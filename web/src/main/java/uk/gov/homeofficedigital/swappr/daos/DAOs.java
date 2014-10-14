package uk.gov.homeofficedigital.swappr.daos;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@Configuration
public class DAOs {

    @Bean
    public HomeDao homeDao(NamedParameterJdbcTemplate jdbcTemplate) {
        return new HomeDao(jdbcTemplate);
    }

    @Bean
    public SwapDao swapDao(NamedParameterJdbcTemplate jdbcTemplate) {
        return new SwapDao(jdbcTemplate);
    }

    @Bean
    public JdbcUserDetailsManager userManager(DataSource ds) {
        JdbcUserDetailsManager mgr = new JdbcUserDetailsManager();
        mgr.setDataSource(ds);
        return mgr;
    }
}
