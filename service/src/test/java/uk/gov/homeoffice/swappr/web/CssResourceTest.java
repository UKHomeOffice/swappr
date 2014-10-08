package uk.gov.homeoffice.swappr.web;

import com.equalexperts.logging.OpsLogger;
import com.equalexperts.logging.OpsLoggerFactory;
import org.eclipse.jetty.http.HttpStatus;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import uk.gov.homeoffice.swappr.web.logging.SwapprLogger;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class CssResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        try {
            OpsLogger<SwapprLogger> logger = new OpsLoggerFactory().build();
            ApplicationConfig config = ApplicationConfig.build(logger, "-server.port","9090","-db.user","SA","-db.url","jdbc:hsqldb:mem:testdb");
            return new JerseyResourceConfig(config, logger);
        } catch(Exception e) {
            throw new RuntimeException("Gah!", e);
        }

    }

    @Test
    public void downloadCss() {
        Response response = target("/css/application.css").request().get();
        assertThat(response.getStatus(), equalTo(HttpStatus.OK_200));
    }
}
