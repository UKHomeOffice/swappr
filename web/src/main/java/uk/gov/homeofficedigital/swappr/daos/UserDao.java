package uk.gov.homeofficedigital.swappr.daos;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;
import uk.gov.homeofficedigital.swappr.model.SwapprUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static uk.gov.homeofficedigital.swappr.daos.DaoUtil.toMap;

public class UserDao implements UserDetailsService {
    private final NamedParameterJdbcTemplate template;

    public UserDao(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<SwapprUser> users = template.query("select username, password, enabled, email, fullname from users where username = :username", toMap("username", username), this::mapUser);

        if (users.isEmpty()) {
            throw new UsernameNotFoundException(String.format("Username %s not found", username));
        }

        SwapprUser user = users.get(0); // contains no GrantedAuthority[]

        Set<GrantedAuthority> dbAuthsSet = new HashSet<GrantedAuthority>(loadUserAuthorities(user.getUsername()));
        List<GrantedAuthority> dbAuths = new ArrayList<GrantedAuthority>(dbAuthsSet);

        if (dbAuths.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User %s has no GrantedAuthority", username));
        }

        return new SwapprUser(username, user.getPassword(), dbAuths,user.getFullname(), user.getEmail(), user.isAccountNonExpired(), user.isAccountNonLocked(), user.isCredentialsNonExpired(), user.isEnabled());
    }

    public void createUser(SwapprUser user) {
        validateUserDetails(user);
        template.update("insert into users (username, password, enabled, email, fullname) values (:username, :password, :enabled, :email, :fullname)",
                toMap("username", user.getUsername(), "password", user.getPassword(), "enabled", user.isEnabled(), "email", user.getEmail(), "fullname", user.getFullname()));

        insertUserAuthorities(user);

    }


    protected List<GrantedAuthority> loadUserAuthorities(String username) {
        return template.query("select username, authority from authorities where username = :username", toMap("username", username), this::mapAuthority);
    }

    private void insertUserAuthorities(SwapprUser user) {
        user.getAuthorities().stream().forEach(grantedAuthority ->
                template.update(
                        "insert into authorities (username, authority) values (:username,:role)",
                        toMap("username", user.getUsername(), "role", grantedAuthority.getAuthority())));
    }

    private void validateUserDetails(UserDetails user) {
        Assert.hasText(user.getUsername(), "Username may not be empty or null");
        validateAuthorities(user.getAuthorities());
    }

    private void validateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Authorities list must not be null");

        for (GrantedAuthority authority : authorities) {
            Assert.notNull(authority, "Authorities list contains a null entry");
            Assert.hasText(authority.getAuthority(), "getAuthority() method must return a non-empty string");
        }
    }

    // # ROW MAPPERS
    private SwapprUser mapUser(ResultSet rs, int row) throws SQLException {
        return new SwapprUser(rs.getString("username"), rs.getString("password"), AuthorityUtils.NO_AUTHORITIES, rs.getString("fullname"), rs.getString("email"), true, true, true, rs.getBoolean("enabled"));
    }

    private SimpleGrantedAuthority mapAuthority(ResultSet rs, int row) throws SQLException {
        return new SimpleGrantedAuthority(rs.getString("authority"));
    }
}
