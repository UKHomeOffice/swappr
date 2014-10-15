package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.boot.autoconfigure.velocity.VelocityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;
import uk.gov.homeofficedigital.swappr.daos.HomeDao;
import uk.gov.homeofficedigital.swappr.daos.SwapDao;
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
        velocityLayoutViewResolver.setAttributesMap(attrs);
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
    public SwapController swap(SwapDao swapDao) {
        return new SwapController(swapDao);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }
}
