package uk.gov.homeofficedigital.swappr.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.homeofficedigital.swappr.SpringIntegrationTest;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MappingTest extends SpringIntegrationTest {

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mvc;

    @Before
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void homePage_isBoundToRoot() throws Exception {
        mvc.perform(get("/").principal(() -> "Freddie"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("You /")));
    }

    @Test
    public void login_isBound() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("login")));
    }

    @Test
    public void userAdmin_isBound() throws Exception {
        mvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("user")));
    }

    @Test
    public void createSwap_isBound() throws Exception {
        mvc.perform(get("/swap"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("swap")));
    }

}