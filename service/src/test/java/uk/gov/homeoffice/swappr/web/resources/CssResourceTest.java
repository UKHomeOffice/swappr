package uk.gov.homeoffice.swappr.web.resources;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class CssResourceTest extends BaseJerseyTest {

    @Ignore
    public void downloadCss() {
        Response response = target("/css/application.css").request().get();
        assertThat(response.getStatus(), equalTo(HttpStatus.OK_200));
    }
}
