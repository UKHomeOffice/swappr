package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.boot.autoconfigure.velocity.VelocityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;
import uk.gov.homeofficedigital.swappr.daos.OfferDao;
import uk.gov.homeofficedigital.swappr.daos.RotaDao;
import uk.gov.homeofficedigital.swappr.daos.ShiftDao;
import uk.gov.homeofficedigital.swappr.service.RotaService;
import uk.gov.homeofficedigital.swappr.spring.VelocityLayoutToolboxView;
import uk.gov.homeofficedigital.swappr.spring.VelocitySecurityHelper;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class Controllers extends WebMvcConfigurerAdapter {

    @Bean
    public VelocityLayoutViewResolver velocityViewResolver(VelocityProperties velocityProperties) {
        VelocityLayoutViewResolver velocityLayoutViewResolver = new VelocityLayoutViewResolver() {
            @Override
            protected Class<?> requiredViewClass() {
                return VelocityLayoutToolboxView.class;
            }
        };
        velocityProperties.applyToViewResolver(velocityLayoutViewResolver);
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("sec", new VelocitySecurityHelper());
        attrs.put("display", new DateDisplay());
        velocityLayoutViewResolver.setAttributesMap(attrs);
        return velocityLayoutViewResolver;
    }

    @Bean
    public UserAdminController userAdmin(JdbcUserDetailsManager userManager, PasswordEncoder encoder) {
        return new UserAdminController(userManager, encoder);
    }


    @Bean
    public SwapController swapController(ShiftDao shiftDao, RotaDao rotaDao, RotaService rotaService, OfferDao offerDao) {
        return new SwapController(shiftDao, rotaDao, rotaService, offerDao);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @Bean
    public HomeController homeController(RotaService rotaService) {
        return new HomeController(rotaService);
    }

    @Bean
    public ShiftController shiftController(ShiftDao shiftDao, RotaDao rotaDao) {
        return new ShiftController(shiftDao, rotaDao);
    }

}
