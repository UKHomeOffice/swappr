package uk.gov.homeofficedigital.swappr.spring;

import org.junit.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import uk.gov.homeofficedigital.swappr.SpringIntegrationTest;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


public class VelocitySecurityHelperTest extends SpringIntegrationTest {

    VelocitySecurityHelper sec = new VelocitySecurityHelper();

    @Test
    public void loggedInUserShouldReturnEmptyWhenNotLoggedIn() {

        logout();

        assertThat(sec.loggedInUser(), equalTo(Optional.empty()));
    }

    @Test
    public void loggedInUserShouldReturnUserWhenLoggedIn() {

        User user = new User("uname", "pword", Arrays.asList(new SimpleGrantedAuthority("MYROLE")));
        login(user);

        assertThat(sec.loggedInUser().get(), equalTo(user));
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