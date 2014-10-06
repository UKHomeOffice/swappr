package uk.gov.homeoffice.swappr.web;

import com.equalexperts.logging.OpsLogger;
import org.junit.Test;
import uk.gov.homeoffice.swappr.web.logging.SwapprLogger;

import java.net.URI;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ApplicationConfigTest {

    @Test
    public void build_shouldConstructTheConfig_givenValidArguments() throws Exception {
        OpsLogger<SwapprLogger> logger = mock(OpsLogger.class);

        ApplicationConfig config = ApplicationConfig.build(logger, "-server.port", "98765", "-db.user", "tom", "-db.url", "some:url");

        assertEquals(new URI("http://0.0.0.0:98765"), config.serverUri());
    }

    @Test
    public void build_shouldLogConfigError_givenInvalidArguments() throws Exception {
        OpsLogger<SwapprLogger> logger = mock(OpsLogger.class);

        try {
            ApplicationConfig.build(logger, "-invalidArg", "some value");
            fail("expected to throw exception");
        } catch (IllegalArgumentException e) {
            // success
        }

        verify(logger).log(eq(SwapprLogger.ConfigurationError), isA(String.class));
    }
}