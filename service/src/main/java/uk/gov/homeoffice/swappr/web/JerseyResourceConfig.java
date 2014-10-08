package uk.gov.homeoffice.swappr.web;


import com.equalexperts.logging.OpsLogger;
import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;
import org.glassfish.jersey.server.ResourceConfig;
import uk.gov.homeoffice.swappr.web.logging.SwapprLogger;
import uk.gov.homeoffice.swappr.web.resources.HealthResource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JerseyResourceConfig extends ResourceConfig {

    public JerseyResourceConfig(ApplicationConfig config, OpsLogger<SwapprLogger> logger) {
        DataSource ds = createDataSource(config, logger);
        register(new HealthResource());
        register(new ThymeleafWriter());
    }

    private DataSource createDataSource(ApplicationConfig config, OpsLogger<SwapprLogger> logger) {
        BoneCPConfig boneConfig = new BoneCPConfig();
        boneConfig.setJdbcUrl(config.getDbUrl());
        boneConfig.setUser(config.getDbUser());
        boneConfig.setPassword(config.getDbPassword());
        BoneCPDataSource ds = new BoneCPDataSource(boneConfig);
        ds.setDefaultAutoCommit(false);
        try (Connection conn = ds.getConnection()) {
            conn.getMetaData();
        } catch (SQLException e) {
            logger.log(SwapprLogger.DatabaseConnectivity, e);
            throw new RuntimeException("Unable to establish database connection", e);
        }
        return ds;
    }


}
