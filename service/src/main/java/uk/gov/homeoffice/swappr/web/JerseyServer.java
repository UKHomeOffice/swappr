package uk.gov.homeoffice.swappr.web;

import com.equalexperts.logging.OpsLogger;
import com.equalexperts.logging.OpsLoggerFactory;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import uk.gov.homeoffice.swappr.web.logging.SwapprLogger;

public class JerseyServer {

    private final ApplicationConfig applicationConfig;
    private final OpsLogger<SwapprLogger> logger;

    public JerseyServer(ApplicationConfig applicationConfig, OpsLogger<SwapprLogger> logger) {
        this.applicationConfig = applicationConfig;
        this.logger = logger;
    }

    public static void main(String[] args) throws Exception {
        OpsLogger<SwapprLogger> logger = new OpsLoggerFactory().build();
        ApplicationConfig config = ApplicationConfig.build(logger, args);
        new JerseyServer(config, logger).run();
    }

    public void run() {
        JerseyResourceConfig jerseyResourceConfig = new JerseyResourceConfig();
        try {
            Server server = JettyHttpContainerFactory.createServer(applicationConfig.serverUri(), jerseyResourceConfig);
            server.join();
        } catch (Exception e) {
            logger.log(SwapprLogger.UnknownError, e);
            throw new RuntimeException(e);
        }
    }
}
