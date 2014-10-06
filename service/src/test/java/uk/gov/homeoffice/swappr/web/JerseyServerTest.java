package uk.gov.homeoffice.swappr.web;

import com.equalexperts.logging.OpsLogger;
import org.junit.Test;
import uk.gov.homeoffice.swappr.web.logging.SwapprLogger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JerseyServerTest {

    @Test
    public void run_shouldLogUnexpectedException_givenAnExceptionIsThrown() throws Exception {
        ApplicationConfig config = mock(ApplicationConfig.class);
        when(config.getDbUrl()).thenReturn("jdbc:hsqldb:mem:testdb");
        when(config.getDbUser()).thenReturn("SA");
        when(config.serverUri()).thenThrow(new IllegalArgumentException("bang"));
        OpsLogger<SwapprLogger> logger = mock(OpsLogger.class);
        JerseyServer jerseyServer = new JerseyServer(logger, config);

        try {
            jerseyServer.run();
            fail("Should have thrown an exception");
        } catch (Exception e) {
            // expected
            verify(logger).log(SwapprLogger.UnknownError, e.getCause());
        }
    }
}