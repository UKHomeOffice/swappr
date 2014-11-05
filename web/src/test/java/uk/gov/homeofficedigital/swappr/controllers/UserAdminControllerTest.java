package uk.gov.homeofficedigital.swappr.controllers;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import uk.gov.homeofficedigital.swappr.daos.UserDao;
import uk.gov.homeofficedigital.swappr.model.SwapprUser;
import uk.gov.homeofficedigital.swappr.model.UserMaker;
import uk.gov.homeofficedigital.swappr.spring.CurrentLoggedInUserArgumentResolver;

import java.util.Arrays;
import java.util.List;

import static com.natpryce.makeiteasy.MakeItEasy.*;
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

    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setValidator(new LocalValidatorFactoryBean())
            .setCustomArgumentResolvers(new CurrentLoggedInUserArgumentResolver())
            .build();

    @Test
    public void userAdmin_shouldDisplayTheUserAdminView() throws Exception {

        mockMvc.perform(get("/admin/users/add"))
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

    @Test
    public void ensureThatGettingResetPasswordPageForUsersShouldContainCorrectPageAndModelAttributesForTheAdminUser() throws Exception {
        // Arrange
        final SwapprUser adminUser = make(a(UserMaker.User, with(UserMaker.username, "admin"), with(UserMaker.authorities, UserMaker.adminAuthority)));
        final SwapprUser bogStandardUser1 = make(a(UserMaker.User, with(UserMaker.username, "user1"), with(UserMaker.fullName, "user 1")));
        final SwapprUser bogStandardUser2 = make(a(UserMaker.User, with(UserMaker.username, "user2"), with(UserMaker.fullName, "user 2")));

        List<SwapprUser> listOfUsers = Arrays.asList(adminUser, bogStandardUser2, bogStandardUser1);

        when(userService.getListOfUsers()).thenReturn(listOfUsers);

        // Act
        mockMvc.perform(get("/admin/users/reset-password").principal(new UsernamePasswordAuthenticationToken(adminUser, null)))
//                .andDo(print())
                .andExpect(view().name("userAdminPasswordReset"))
                .andExpect(status().isOk())
                .andExpect(model().size(2))
                .andExpect(model().attribute("userMap", allOf(
                        hasEntry(bogStandardUser2.getUsername(), bogStandardUser2.getFullname()),
                        hasEntry(bogStandardUser1.getUsername(), bogStandardUser1.getFullname()),
                        not(hasEntry(adminUser.getUsername(), adminUser.getFullname()))
                )))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("password", Matchers.nullValue()),
                        hasProperty("confirmPassword", Matchers.nullValue()))))
        ;
    }

    @Test
    public void ensureThatGettingResetPasswordPageForUsersShouldContainOnlyTheCurrentUserForSelectionOnTheModel() throws Exception {
        // Arrange
        final SwapprUser bogStandardUser3 = make(a(UserMaker.User, with(UserMaker.username, "user 3"), with(UserMaker.authorities, UserMaker.userAuthority)));
        final SwapprUser bogStandardUser1 = make(a(UserMaker.User, with(UserMaker.username, "user1"), with(UserMaker.fullName, "user 1"), with(UserMaker.authorities, UserMaker.userAuthority)));
        final SwapprUser bogStandardUser2 = make(a(UserMaker.User, with(UserMaker.username, "user2"), with(UserMaker.fullName, "user 2"), with(UserMaker.authorities, UserMaker.userAuthority)));

        List<SwapprUser> listOfUsers = Arrays.asList(bogStandardUser3, bogStandardUser2, bogStandardUser1);

        when(userService.getListOfUsers()).thenReturn(listOfUsers);

        // Act
        mockMvc.perform(get("/admin/users/reset-password").principal(new UsernamePasswordAuthenticationToken(bogStandardUser3, null)))
//                .andDo(print())
                .andExpect(view().name("userAdminPasswordReset"))
                .andExpect(status().isOk())
                .andExpect(model().size(2))
                .andExpect(model().attribute("userMap", allOf(
                        hasEntry(bogStandardUser3.getUsername(), bogStandardUser3.getFullname()),
                        not(hasEntry(bogStandardUser2.getUsername(), bogStandardUser2.getFullname())),
                        not(hasEntry(bogStandardUser1.getUsername(), bogStandardUser1.getFullname()))
                )))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("password", Matchers.nullValue()),
                        hasProperty("confirmPassword", Matchers.nullValue()))))
        ;
    }
}