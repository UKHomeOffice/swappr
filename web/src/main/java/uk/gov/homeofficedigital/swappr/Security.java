package uk.gov.homeofficedigital.swappr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import uk.gov.homeofficedigital.swappr.daos.UserDao;
import uk.gov.homeofficedigital.swappr.model.Role;

@Configuration
@EnableWebMvcSecurity
public class Security extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasAuthority(Role.ADMIN.name())
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .anyRequest().authenticated();
        http.formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();
        http.csrf().disable();
        http.rememberMe().key("swapprUser");
    }


    @Bean
    public PasswordEncoder encoder() {
        return new StandardPasswordEncoder("it's a secret");
    }


    @Autowired
    public void globalSecurityConfiguration(AuthenticationManagerBuilder auth, UserDetailsService userDetailsService, PasswordEncoder encoder) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
    }

    //
//    @Configuration
//    protected static class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {
//
//        @Autowired
//        private DataSource dataSource;
//
//        private PasswordEncoder passwordEncoder = new StandardPasswordEncoder("it's a secret");
//
//        @Bean
//        public PasswordEncoder encoder() {
//            return passwordEncoder;
//        }
//
//        @Bean
//        public UserDetailsService userDetailsService(){
//            return new UserDao(template)
//        }
//
//        @Override
//        public void init(AuthenticationManagerBuilder auth) throws Exception {
////            auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder);
//            auth.userDetailsService(userDetailsService()).passwordEncoder(encoder());
//        }
//    }
}
