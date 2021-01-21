package by.andd3dfx.configs;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    @Autowired
    DataSource dataSource;

    /**
     * Designed according to article from 'Spring in action' book, p.4.2.2
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery("select USERNAME, PASSWORD, ENABLED from SECURED_USERS where USERNAME=?")
            .authoritiesByUsernameQuery("select USERNAME, ROLE from SECURED_USERS where username=?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            // HTTP Basic authentication
            .httpBasic()
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/api/v1/articles/**").hasAnyRole("ADMIN", "USER")
            .antMatchers(HttpMethod.POST, "/api/v1/articles").hasRole("ADMIN")
            .antMatchers(HttpMethod.PATCH, "/api/v1/articles/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.DELETE, "/api/v1/articles/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.GET, "/api/v1/authors/**").hasAnyRole("ADMIN", "USER")
            .and()
            .csrf().disable()
            .formLogin().disable();
    }
}
