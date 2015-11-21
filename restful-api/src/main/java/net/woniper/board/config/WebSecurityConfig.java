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
import org.springframework.security.web.header.writers.StaticHeadersWriter;

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
            .httpBasic()
            .and()
            .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/users").permitAll()
                .antMatchers("/swagger", "/swagger/index.html").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/kindBoards/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/kindBoards/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/kindBoards/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/kindBoards/**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/users/**", "/users/boards/**", "/test").hasAnyRole("ADMIN", "USER")
                .antMatchers("/uploads/**", "/download/**", "/file-update/**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST).hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.PUT).hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.DELETE).hasAnyRole("ADMIN", "USER")
                .anyRequest().permitAll()
            .and()
                .formLogin().loginPage("/login")
                    .failureUrl("/login?error").defaultSuccessUrl("/")
            .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/").permitAll()
            .and()
                // 동시 로그인 세션 : 1, 중복 로그인시 기존 세션 종료 후 url : /
                .sessionManagement().maximumSessions(1).expiredUrl("/");

        // X-Frame-Option
        http.authorizeRequests()
                .anyRequest().authenticated()
            .and()
                .headers()
                .addHeaderWriter(new StaticHeadersWriter("X-Content-Secutiry-Policy", "script-src 'self'"))
                .frameOptions().disable();
    }
}
