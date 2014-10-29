package uk.gov.homeofficedigital.swappr.daos;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import uk.gov.homeofficedigital.swappr.SpringIntegrationTest;
import uk.gov.homeofficedigital.swappr.model.SwapprUser;

import java.util.Map;

import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static uk.gov.homeofficedigital.swappr.model.UserMaker.*;

public class SwapprUserDaoTest extends SpringIntegrationTest{

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private UserDao userDao;

    @Test
    public void ensureThat_aUserIsCreatedAndPersisted() {
        // Arrange
        SwapprUser user = make(a(User, with(username, RandomStringUtils.randomAlphanumeric(50)), with(password, RandomStringUtils.randomAlphanumeric(250))));

        // Act
        userDao.createUser(user);

        //Assert
        final Map<String, Object> userResultMap = template.queryForMap("select * from users where username = ?", user.getUsername());
        final Map<String, Object> roleResultMap = template.queryForMap("select * from authorities where username = ?", user.getUsername());

        assertFalse(userResultMap.isEmpty());
        assertThat(userResultMap.get("username"), is(user.getUsername()));
        assertThat(userResultMap.get("password"), is(user.getPassword()));

        assertFalse(roleResultMap.isEmpty());
        assertThat(roleResultMap.get("authority"), is(user.getAuthorities().iterator().next().getAuthority()));
    }

}