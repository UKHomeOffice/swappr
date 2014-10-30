package uk.gov.homeofficedigital.swappr.controllers;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import uk.gov.homeofficedigital.swappr.daos.UserDao;
import uk.gov.homeofficedigital.swappr.model.SwapprUser;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserAdminControllerTest {

    private PasswordEncoder encoder = mock(PasswordEncoder.class);
    private UserDao userService = mock(UserDao.class);
    private UserAdminController controller = new UserAdminController(userService, encoder);

    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).setValidator(new LocalValidatorFactoryBean()).build();

    @Test
    public void userAdmin_shouldDisplayTheUserAdminView() throws Exception {

        mockMvc.perform(get("/admin/users"))
                .andExpect(view().name("userAdmin"))
                .andExpect(status().isOk())
                .andExpect(model().size(1))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("username", nullValue()),
                        hasProperty("fullname", nullValue()),
                        hasProperty("password", nullValue()),
                        hasProperty("confirmPassword", nullValue()),
                        hasProperty("email", nullValue())
                )));
    }

    @Test
    public void add_shouldRedisplayTheForm_givenParametersWereInvalid() throws Exception {
        mockMvc.perform(post("/admin/users/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("userAdmin"))
                .andExpect(model().size(1))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeHasFieldErrors("user", "username", "fullname", "password", "confirmPassword", "email"));
    }

    @Test
    public void add_shouldCreateTheUser_givenParametersWereValid() throws Exception {
        when(encoder.encode("pwd1")).thenReturn("encodedPwd1");

        mockMvc.perform(post("/admin/users/add")
                        .param("username", "someUser")
                        .param("password", "pwd1")
                        .param("confirmPassword", "pwd1")
                        .param("fullname", "Bob Smith")
                        .param("email", "bob@mail.com")
        )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(model().size(1))
                .andExpect(model().hasNoErrors()
                );

        ArgumentCaptor<SwapprUser> userCaptor = ArgumentCaptor.forClass(SwapprUser.class);

        verify(userService).createUser(userCaptor.capture());
        SwapprUser actualUser = userCaptor.getValue();

        assertThat(actualUser.getUsername(), is("someUser"));
        assertThat(actualUser.getEmail(), is("bob@mail.com"));
        assertThat(actualUser.getPassword(), is("encodedPwd1"));
        assertThat(actualUser.getFullname(), is("Bob Smith"));
        assertThat(actualUser.getAuthorities(), contains(new SimpleGrantedAuthority("USER")));
    }
}