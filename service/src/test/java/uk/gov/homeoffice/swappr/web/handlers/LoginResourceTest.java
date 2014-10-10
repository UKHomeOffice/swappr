package uk.gov.homeoffice.swappr.web.handlers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.homeoffice.swappr.model.User;
import uk.gov.homeoffice.swappr.repository.UserRepository;
import uk.gov.homeoffice.swappr.web.resources.Viewable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginResourceTest {

    public static final String PASSWORD = "password";
    public static final String NAME = "name";

    private UserRepository repo = mock(UserRepository.class);
    private LoginResource resource;

    @Before
    public void setup() {
        resource = new LoginResource(repo);
    }

    @Test
    public void get_shouldReturnUserLoginPage_givenLoginRequest() throws Exception {
        Viewable viewable = resource.get();

        assertThat(viewable.getView(), is("login"));
    }

    @Test
    public void login_shouldAddUserToSessionAndRedirectToHome_givenValidUsernamePassword() throws Exception {
        User user = new User();
        when(repo.findUser(NAME, PASSWORD)).thenReturn(user);

        Response result = resource.login(NAME, PASSWORD);

        assertEquals(303, result.getStatus());
        assertEquals("/", result.getLocation().toString());
        assertTrue(result.getCookies().containsKey("user"));
        assertEquals(String.valueOf(user.getId()), result.getCookies().get("user").getValue());
    }


    @Test
    public void login_shouldRedirectBackToLogin_givenUsernameOrPasswordNotValid() throws Exception {
        when(repo.findUser(NAME, PASSWORD)).thenReturn(null);

        Response result = resource.login(NAME, PASSWORD);

        
    }

}