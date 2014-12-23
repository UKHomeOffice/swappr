package uk.gov.homeofficedigital.swappr.daos;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import uk.gov.homeofficedigital.swappr.SpringIntegrationTest;
import uk.gov.homeofficedigital.swappr.controllers.exceptions.UserPasswordResetNotAllowedException;
import uk.gov.homeofficedigital.swappr.model.Role;
import uk.gov.homeofficedigital.swappr.model.SwapprUser;
import uk.gov.homeofficedigital.swappr.model.UserMaker;

import java.util.Map;

import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static uk.gov.homeofficedigital.swappr.model.UserMaker.*;

public class SwapprUserDaoTest extends SpringIntegrationTest {

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private UserDao userDao;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Test
    public void ensureThat_aUserIsPersistedCorrectlyWhenCreatingAUser() {
        // Arrange
        SwapprUser user = make(a(User, with(username, RandomStringUtils.randomAlphanumeric(50)),
                with(password, RandomStringUtils.randomAlphanumeric(250)),
                with(fullName, RandomStringUtils.randomAlphanumeric(150)),
                with(emailAddress, RandomStringUtils.randomAlphanumeric(150))));

        // Act
        userDao.createUser(user);

        //Assert
        final Map<String, Object> userResultMap = template.queryForMap("select * from users where username = ?", user.getUsername());
        final Map<String, Object> roleResultMap = template.queryForMap("select * from authorities where username = ?", user.getUsername());

        assertFalse(userResultMap.isEmpty());
        assertThat(userResultMap.get("username"), is(user.getUsername()));
        assertThat(userResultMap.get("password"), is(user.getPassword()));
        assertThat(userResultMap.get("email"), is(user.getEmail()));
        assertThat(userResultMap.get("fullname"), is(user.getFullname()));

        assertFalse(roleResultMap.isEmpty());
        assertThat(roleResultMap.get("authority"), is(user.getAuthorities().iterator().next().getAuthority()));
    }

    @Test
    public void ensureThatWhenUserCannotBeFoundThatUserNotFoundExceptionIsThrown() {
        // Arrange
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage("Username bogusUser not found");
        // Act

        userDao.loadUserByUsername("bogusUser");

        //Assert
    }

    // This is to replicate the same behaviour we used to have when we were using Springs UserDetailsManager Class.
    @Test
    public void ensureThatWhenLoadingAUserIfTheyHaveNoRolesThenTreatThemAsIfTheyCannotBeFound() {
        // Arrange
        String username = RandomStringUtils.randomAlphanumeric(50);
        template.update("insert into users(username, password, enabled, email, fullname) values ('" + username + "', 'password', 1, 'mail@mail.com', 'Test User')");
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage("User " + username + " has no GrantedAuthority");
        // Act

        userDao.loadUserByUsername(username);

        //Assert
    }

    @Test
    public void ensureThatLoadingUserByUsername_PopulatesFullnameAndEmailProperties() {
        // Arrange
        String username = RandomStringUtils.randomAlphanumeric(50);
        template.update("insert into users(username, password, enabled, email, fullname) values ('" + username + "', 'password', 1, 'mail@mail.com', 'Test User')");
        template.update("insert into authorities(username, authority) values ('" + username + "', 'USER')");

        // Act

        final SwapprUser userDetails = (SwapprUser) userDao.loadUserByUsername(username);

        //Assert
        assertThat(userDetails.getFullname(), is("Test User"));
        assertThat(userDetails.getEmail(), is("mail@mail.com"));
    }


    @Test
    public void ensureThatUserPasswordResetUpdatesTheUsersPassword() {
        // Arrange
        String adminUsername = RandomStringUtils.randomAlphanumeric(25);
        SwapprUser adminUser = make(a(UserMaker.User, with(UserMaker.authorities, UserMaker.adminAuthority), with(UserMaker.username, adminUsername)));
        template.update("insert into users(username, password, enabled, email, fullname) values (?,?,?,?,?)",
                adminUsername,
                adminUser.getPassword(),
                adminUser.isEnabled(),
                adminUser.getEmail(),
                adminUser.getFullname());

        template.update("insert into authorities(username, authority) values (?,?)", adminUsername, Role.ADMIN.name());

        String normalUsername = RandomStringUtils.randomAlphanumeric(25);
        template.update("insert into users(username, password, enabled, email, fullname) values (?,?,?,?,?)",
                normalUsername,
                "test_user_password",
                true,
                normalUsername + "@mail.com",
                "fullname"
        );
        template.update("insert into authorities(username, authority) values (?,?)",
                normalUsername,
                Role.USER.name());


        // Act
        userDao.resetUserPassword(adminUser, normalUsername, "NEW_PASSWORD_RESET");

        final Map<String, Object> userResultMap = template.queryForMap("select * from users where username = ?", normalUsername);
        //Assert

        assertFalse(userResultMap.isEmpty());
        assertThat(userResultMap.get("password"), is("NEW_PASSWORD_RESET"));
    }

    @Test
    public void ensureThatIfAttemptingToResetAnotherUserPasswordAndYouAreNotAdminThenThrowAnException() {
        // Arrange
        expectedException.expect(UserPasswordResetNotAllowedException.class);
        expectedException.expectMessage(is("Only Administrators can change passwords of other users."));

        String username_1 = RandomStringUtils.randomAlphanumeric(25);
        SwapprUser loggedInUser = make(a(UserMaker.User, with(UserMaker.authorities, UserMaker.userAuthority), with(UserMaker.username, username_1)));
        template.update("insert into users(username, password, enabled, email, fullname) values (?,?,?,?,?)",
                username_1,
                loggedInUser.getPassword(),
                loggedInUser.isEnabled(),
                loggedInUser.getEmail(),
                loggedInUser.getFullname());

        template.update("insert into authorities(username, authority) values (?,?)",
                username_1,
                Role.USER.name());

        String normalUsername = RandomStringUtils.randomAlphanumeric(25);
        template.update("insert into users(username, password, enabled, email, fullname) values (?,?,?,?,?)",
                normalUsername,
                "test_user_password",
                true,
                normalUsername + "@mail.com",
                "fullname"
        );
        template.update("insert into authorities(username, authority) values (?,?)",
                normalUsername,
                Role.USER.name());


        // Act
        userDao.resetUserPassword(loggedInUser, normalUsername, "NEW_PASSWORD_RESET");
    }
}