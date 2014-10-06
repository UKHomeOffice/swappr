package uk.gov.homeoffice.swappr.web;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class ApplicationConfig {

//    @Parameter(names = "-server.port", description = "port for web service to listen on", required = true)
    private int port = 9090;


    public URI serverUri() {
        return UriBuilder.fromUri("http://0.0.0.0").port(port).build();
    }

    public static ApplicationConfig build(String... args) {
        return new ApplicationConfig();
    }
}
