package uk.gov.homeofficedigital.swappr.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SwapprUser implements UserDetails{


    private final String fullname;
    private final String email;
    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;


    @Deprecated
    public SwapprUser(String username, String password, List<GrantedAuthority> authorities) {
        this(username,password,authorities, "N/A", "unknown@email.com");
    }

    public SwapprUser(String username, String password, List<GrantedAuthority> authorities, String fullname, String email) {
        this(username,password,authorities, fullname, email, true, true, true,true);
    }

    @Deprecated
    public SwapprUser(String username, String password, List<GrantedAuthority> authorities, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        this(username,password,authorities, "N/A", "unknown@mail.com",accountNonExpired,accountNonLocked,credentialsNonExpired,enabled);
    }


    public SwapprUser(String username, String password, List<GrantedAuthority> authorities, String fullname, String email, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.fullname = fullname;
        this.email = email;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableList(authorities);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Returns {@code true} if the supplied object is a {@code SwapprUser} instance with the
     * same {@code username} value.
     * <p>
     * In other words, the objects are equal if they have the same username, representing the
     * same principal.
     */
    @Override
    public boolean equals(Object rhs) {
        if (rhs instanceof SwapprUser) {
            return username.equals(((SwapprUser) rhs).username);
        }
        return false;
    }

    /**
     * Returns the hashcode of the {@code username}.
     */
    @Override
    public int hashCode() {
        return username.hashCode();
    }

    public String getEmail() {
        return email;
    }

    public String getFullname() {
        return fullname;
    }
}
