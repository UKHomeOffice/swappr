package uk.gov.homeofficedigital.swappr.spring;

import org.junit.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import uk.gov.homeofficedigital.swappr.SpringIntegrationTest;
import uk.gov.homeofficedigital.swappr.model.Role;

import java.util.Arrays;
import java.util.Optional;

import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.homeofficedigital.swappr.model.UserMaker.*;

public class VelocitySecurityHelperTest extends SpringIntegrationTest {

    private VelocitySecurityHelper sec = new VelocitySecurityHelper();

    @Test
    public void loggedInUserShouldReturnEmptyWhenNotLoggedIn() {

        logout();

        assertThat(sec.loggedInUser(), equalTo(Optional.empty()));
    }

    @Test
    public void loggedInUserShouldReturnUserWhenLoggedIn() {

        User user = make(a(User, with(authorities, userAuthority)));
        login(user);

        assertThat(sec.loggedInUser().get(), equalTo(user));
    }

    @Test
    public void hasAuthorityShouldTestForAuthorityName() {
        User user = make(a(User, with(authorities, adminAuthority)));
        login(user);

        assertThat(sec.hasAuthority(Role.ADMIN), equalTo(true));
        assertThat(sec.hasAuthority(Role.USER), equalTo(false));
    }


    private void login(User user) {
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void logout() {
        Authentication auth = new AnonymousAuthenticationToken("key", "anon", Arrays.asList(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}