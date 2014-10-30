package uk.gov.homeofficedigital.swappr.controllers;

import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import uk.gov.homeofficedigital.swappr.controllers.forms.UserForm;
import uk.gov.homeofficedigital.swappr.daos.UserDao;
import uk.gov.homeofficedigital.swappr.model.SwapprUser;

import java.util.Arrays;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class UserAdminControllerTest {

    private PasswordEncoder encoder = mock(PasswordEncoder.class);
    private UserDao userService = mock(UserDao.class);
    private UserAdminController controller = new UserAdminController(userService, encoder);

    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    @Test
    public void userAdmin_shouldDisplayTheUserAdminView() throws Exception {

        mockMvc.perform(get("/admin/users"))
                .andExpect(view().name("userAdmin"))
                .andExpect(model().size(1))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("username", nullValue()),
                        hasProperty("fullname", nullValue()),
                        hasProperty("password", nullValue()),
                        hasProperty("confirmPassword", nullValue()),
                        hasProperty("email", nullValue())
                        )));

//        String viewName = controller.userAdmin(model);
//
//        assertEquals("userAdmin", viewName);
//        verify(model).addAttribute(eq("user"), isA(UserForm.class));
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