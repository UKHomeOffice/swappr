package uk.gov.homeoffice.swappr.web;

import com.equalexperts.logging.OpsLogger;
import org.junit.Test;
import uk.gov.homeoffice.swappr.web.logging.SwapprLogger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JerseyResourceConfigTest {

    @Test
    public void config_shouldLogDatabaseConnectivityError_givenUnableToConnectToDB() throws Exception {
        OpsLogger<SwapprLogger> logger = mock(OpsLogger.class);
        ApplicationConfig config = ApplicationConfig.build(logger, "-server.port", "98765", "-db.url", "bogus:url", "-db.user", "user");

        try {
            new JerseyResourceConfig(config, logger);
            fail("Should throw exception");
        } catch (Exception e) {
            // expected
            verify(logger).log(SwapprLogger.DatabaseConnectivity, e.getCause());
        }
    }

}