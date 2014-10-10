package uk.gov.homeofficedigital.swappr.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import uk.gov.homeofficedigital.swappr.daos.HomeDao;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = HomeControllerTest.TestApplication.class)
public class HomeControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mvc;

    @Before
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void hello() throws Exception {
        mvc.perform(get("/")).andExpect(status().isOk()).andExpect(content().string(containsString("Jonnie")));
    }

    @Configuration
    @EnableWebMvc
    @EnableAutoConfiguration
    @Import({Controllers.class, DummyDAOs.class})
    public static class TestApplication {

    }
    @Configuration
    public static class DummyDAOs {

        @Bean
        public HomeDao homeDao() {
            return new HomeDao(null) {
                @Override
                public String name() {
                    return "Jonnie";
                }
            };
        }
    }
}