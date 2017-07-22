package ru.com.m74.cw.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import ru.com.m74.cw.spring.dao.SecurityDao;

import javax.servlet.http.HttpServletResponse;

/**
 * @author mixam
 * @since 10.05.17 14:55
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityDao securityDao;

    @Bean
    protected AuthenticationEntryPoint entryPoint() {
        return (request, response, authException) -> {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http
                .authorizeRequests()
                .antMatchers(
                        "/**/index.html",
                        "/**/cache.appcache",
                        "/favicon.ico",
                        "/**/*.js",
                        "/**/*.json",
                        "/**/*.gif",
                        "/**/*.png",
                        "/**/*.css"
                ).permitAll()
                .anyRequest().authenticated();

        http.exceptionHandling().authenticationEntryPoint(entryPoint());

        http
                .formLogin()
                .loginProcessingUrl("/security/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .failureHandler((request, response, exception) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                })
                .successHandler((request, response, authentication) -> {
                    response.setContentType("application/json");
                    response.getWriter().print("{id:'test'}");
                    response.getWriter().close();
                })

                .permitAll();

        http
                .logout()
                .permitAll()
                .logoutUrl("/security/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                });
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .userDetailsService(securityDao);


    }

}
