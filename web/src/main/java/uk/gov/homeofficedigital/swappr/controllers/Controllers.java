package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.boot.autoconfigure.velocity.VelocityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;
import uk.gov.homeofficedigital.swappr.daos.HomeDao;

@Configuration
public class Controllers extends WebMvcConfigurerAdapter {

    @Bean
    public VelocityLayoutViewResolver velocityViewResolver(VelocityProperties velocityProperties) {
        VelocityLayoutViewResolver velocityLayoutViewResolver = new VelocityLayoutViewResolver();
        velocityProperties.applyToViewResolver(velocityLayoutViewResolver);
        return velocityLayoutViewResolver;
    }

    @Bean
    public HomeController home(HomeDao dao) {
        return new HomeController(dao);
    }

    @Bean
    public UserAdminController userAdmin(JdbcUserDetailsManager userManager, PasswordEncoder encoder) {
        return new UserAdminController(userManager, encoder);
    }

    @Bean
    public SwapController swap() {
        return new SwapController();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }
}
