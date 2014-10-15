package uk.gov.homeofficedigital.swappr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import uk.gov.homeofficedigital.swappr.model.Role;

import javax.sql.DataSource;

@Configuration
@EnableWebMvcSecurity
public class Security extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
            .antMatchers("/css/**").permitAll()
            .antMatchers("/js/**").permitAll()
            .antMatchers("/images/**").permitAll()
            .anyRequest().authenticated();
        http.formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/", true)
            .permitAll()
            .and()
            .logout()
            .permitAll();
        http.csrf().disable();
        http.rememberMe().key("swapprUser");
    }

    @Configuration
    protected static class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {

        @Autowired
        private DataSource dataSource;

        private PasswordEncoder passwordEncoder = new StandardPasswordEncoder("it's a secret");

        @Bean
        public PasswordEncoder encoder() {
            return passwordEncoder;
        }

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication().withUser("admin").password("admin").authorities(Role.USER.name(), Role.ADMIN.name());
            auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder);
        }
    }
}
