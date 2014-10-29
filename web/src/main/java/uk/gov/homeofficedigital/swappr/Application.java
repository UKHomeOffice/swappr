package uk.gov.homeofficedigital.swappr;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.gov.homeofficedigital.swappr.controllers.Controllers;
import uk.gov.homeofficedigital.swappr.daos.DAOs;
import uk.gov.homeofficedigital.swappr.service.Services;

@Configuration
@EnableAutoConfiguration
@Import({DAOs.class, Security.class, Controllers.class, Services.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
