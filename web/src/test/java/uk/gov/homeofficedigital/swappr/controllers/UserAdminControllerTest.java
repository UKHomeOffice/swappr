package uk.gov.homeofficedigital.swappr.controllers;

import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import uk.gov.homeofficedigital.swappr.controllers.forms.UserForm;
import uk.gov.homeofficedigital.swappr.daos.UserDao;
import uk.gov.homeofficedigital.swappr.model.SwapprUser;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

public class UserAdminControllerTest {

    private PasswordEncoder encoder = mock(PasswordEncoder.class);
    private UserDao userService = mock(UserDao.class);
    private UserAdminController controller = new UserAdminController(userService, encoder);

    @Test
    public void userAdmin_shouldDisplayTheUserAdminView() throws Exception {
        Model model = mock(Model.class);

        String viewName = controller.userAdmin(model);

        assertEquals("userAdmin", viewName);
        verify(model).addAttribute(eq("user"), isA(UserForm.class));
    }

    @Test
    public void add_shouldRedisplayTheForm_givenParametersWereInvalid() throws Exception {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        String target = controller.add(new UserForm(), result);

        assertEquals("userAdmin", target);
    }

    @Test
    public void add_shouldCreateTheUser_givenParametersWereValid() throws Exception {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        UserForm form = new UserForm();
        form.setUsername("someUser");
        form.setPassword("pwd1");
        form.setConfirmPassword("pwd1");
        when(encoder.encode("pwd1")).thenReturn("encodedPwd1");

        String target = controller.add(form, result);

        assertEquals("redirect:/", target);
        verify(userService).createUser(new SwapprUser("someUser", "encodedPassword", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));
    }
}