package uk.gov.homeoffice.swappr.web.resources;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class HealthResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new Application() {
            @Override
            public Set<Object> getSingletons() {
                return new HashSet<Object>(Arrays.asList(new HealthResource()));
            }
        };
    }

    @Test
    public void ping_shouldReturnPong() throws Exception {
        Response result = this.client().target(getBaseUri()).path("/ping").request().get();

        assertEquals(200, result.getStatus());
        assertEquals("pong", result.readEntity(String.class));
    }
}