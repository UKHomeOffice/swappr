package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.homeofficedigital.swappr.daos.HomeDao;

@Configuration
public class Controllers {

    @Bean
    public HomeController home(HomeDao dao) {
        return new HomeController(dao);
    }
}
