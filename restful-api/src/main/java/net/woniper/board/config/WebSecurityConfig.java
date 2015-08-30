package net.woniper.board.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
* Created by woniper on 15. 1. 28..
*/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
        .authorizeRequests()
            .antMatchers("/swagger", "/swagger/index.html").hasRole("ADMIN")
            .antMatchers(HttpMethod.GET, "/users/boards/**").hasAnyRole("ADMIN", "USER")
            .antMatchers(HttpMethod.POST).hasAnyRole("ADMIN", "USER")
            .antMatchers(HttpMethod.PUT).hasAnyRole("ADMIN", "USER")
            .antMatchers(HttpMethod.DELETE).hasAnyRole("ADMIN", "USER")
            .antMatchers(HttpMethod.POST, "/users").permitAll()
            .anyRequest().permitAll()
                .and()
            .formLogin().loginPage("/login").failureUrl("/login?error").permitAll()
                .and()
            .logout().logoutUrl("/logout").logoutSuccessUrl("/").permitAll();
    }
}
