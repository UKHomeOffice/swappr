package uk.gov.homeoffice.swappr.web.resources;

import com.equalexperts.logging.OpsLogger;
import com.equalexperts.logging.OpsLoggerFactory;
import org.glassfish.jersey.test.JerseyTest;
import uk.gov.homeoffice.swappr.web.ApplicationConfig;
import uk.gov.homeoffice.swappr.web.JerseyResourceConfig;
import uk.gov.homeoffice.swappr.web.logging.SwapprLogger;

import javax.ws.rs.core.Application;

public abstract class BaseJerseyTest extends JerseyTest {

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

}
