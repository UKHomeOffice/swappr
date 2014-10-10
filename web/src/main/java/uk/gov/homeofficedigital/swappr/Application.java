package uk.gov.homeofficedigital.swappr;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.gov.homeofficedigital.swappr.controllers.Controllers;
import uk.gov.homeofficedigital.swappr.daos.DAOs;

@Configuration
@EnableAutoConfiguration
@Import({Controllers.class, DAOs.class, Security.class, Security.AuthenticationConfiguration.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
