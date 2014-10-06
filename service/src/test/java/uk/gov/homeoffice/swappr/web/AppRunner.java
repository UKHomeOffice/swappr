package uk.gov.homeoffice.swappr.web;

import com.equalexperts.logging.OpsLogger;
import com.equalexperts.logging.OpsLoggerFactory;
import uk.gov.homeoffice.swappr.web.logging.SwapprLogger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.stream.Collectors;

public class AppRunner {

    public static void main(String[] args) throws Exception {
        Connection db = DriverManager.getConnection("jdbc:hsqldb:mem:testdb", "SA", "");
        InputStream inputStream = AppRunner.class.getResourceAsStream("/schema.sql");
        String sql = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        Statement statement = db.createStatement();
        statement.execute(sql);
        statement.close();
        OpsLogger<SwapprLogger> logger = new OpsLoggerFactory().build();

        ApplicationConfig config = ApplicationConfig.build(logger, "@" + AppRunner.class.getResource("/config.properties").getFile());
        new JerseyServer(logger, config).run();
    }
}
