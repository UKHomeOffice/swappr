package uk.gov.homeofficedigital.swappr;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("integrationTest")
public abstract class SpringIntegrationTest {

    @Autowired JdbcTemplate template;

    @Before
    public void setUp() {
        template.execute("delete from volunteer");
        template.execute("delete from offer");
        template.execute("delete from rota");
    }
}
