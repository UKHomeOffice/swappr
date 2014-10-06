package uk.gov.homeoffice.swappr.web;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.equalexperts.logging.OpsLogger;
import uk.gov.homeoffice.swappr.web.logging.SwapprLogger;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class ApplicationConfig {

    @Parameter(names = "-server.port", description = "port for web service to listen on", required = true)
    private int port;
    @Parameter(names = "-db.user", description = "username for database connection", required = true)
    private String dbUser;
    @Parameter(names = "-db.url", description = "JDBC url for database connection", required = true)
    private String dbUrl;
    @Parameter(names = "-db.password", description = "Password for database connection", required = false)
    private String dbPassword;

    public URI serverUri() {
        return UriBuilder.fromUri("http://0.0.0.0").port(port).build();
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public static ApplicationConfig build(OpsLogger<SwapprLogger> logger, String... args) {
        ApplicationConfig config = new ApplicationConfig();
        JCommander jCommander = new JCommander(config);
        try {
            jCommander.parse(args);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            jCommander.usage(sb);
            logger.log(SwapprLogger.ConfigurationError, sb.toString());
            throw new IllegalArgumentException("Configuration error, usage: \n" + sb.toString(), e);
        }
        return config;
    }
}
