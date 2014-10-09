package uk.gov.homeofficedigital.swappr.daos;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class DAOs {

    @Bean
    public HomeDao homeDao(NamedParameterJdbcTemplate jdbcTemplate) {
        return new HomeDao(jdbcTemplate);
    }
}
