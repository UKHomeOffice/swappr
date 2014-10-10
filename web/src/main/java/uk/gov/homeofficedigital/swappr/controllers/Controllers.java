package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import uk.gov.homeofficedigital.swappr.daos.HomeDao;

@Configuration
public class Controllers extends WebMvcConfigurerAdapter {

    @Bean
    public HomeController home(HomeDao dao) {
        return new HomeController(dao);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }
}
