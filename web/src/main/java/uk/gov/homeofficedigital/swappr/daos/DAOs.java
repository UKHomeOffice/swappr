package uk.gov.homeofficedigital.swappr.daos;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;

@Configuration
public class DAOs {

    @Bean
    public RotaDao rotaDao(NamedParameterJdbcTemplate jdbcTemplate, UserDetailsManager userService) {
        return new RotaDao(jdbcTemplate, userService);
    }

    @Bean
    public OfferDao offerDao(NamedParameterJdbcTemplate jdbcTemplate, RotaDao rotaDao) {
        return new OfferDao(jdbcTemplate, rotaDao);
    }

    @Bean
    public VolunteerDao volunteerDao(NamedParameterJdbcTemplate jdbcTemplate, OfferDao offerDao, RotaDao rotaDao) {
        return new VolunteerDao(jdbcTemplate, offerDao, rotaDao);
    }

    @Bean
    public JdbcUserDetailsManager userManager(DataSource ds) {
        JdbcUserDetailsManager mgr = new JdbcUserDetailsManager();
        mgr.setDataSource(ds);
        return mgr;
    }
}
