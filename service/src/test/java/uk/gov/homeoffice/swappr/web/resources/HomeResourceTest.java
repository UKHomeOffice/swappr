package uk.gov.homeoffice.swappr.web.resources;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class HomeResourceTest extends BaseJerseyTest {

    @Test
    public void homePageShouldReturnOK() {
        Response response = target("/").request().get();
        assertThat(response.getStatus(), equalTo(HttpStatus.OK_200));
    }

}
