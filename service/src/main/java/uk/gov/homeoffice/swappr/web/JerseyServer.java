package uk.gov.homeoffice.swappr.web;

import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;

public class JerseyServer {

    private final ApplicationConfig applicationConfig;
    public Server server;

    public JerseyServer(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    public static void main(String[] args) throws Exception {
        ApplicationConfig config = ApplicationConfig.build(args);
        new JerseyServer(config).run();
    }

    public void run() {
        JerseyResourceConfig jerseyResourceConfig = new JerseyResourceConfig();
        try {
            server = JettyHttpContainerFactory.createServer(applicationConfig.serverUri(), jerseyResourceConfig);
            server.join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
