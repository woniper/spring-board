package net.woniper.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.persistence.EntityManagerFactory;

/**
 * Created by woniper on 15. 1. 26..
 */
@SpringBootApplication
public class BoardApplication {

    @Bean
    WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurerAdapter() {

            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("/board.html");
                registry.addViewController("/dashboard").setViewName("/dashboard.html");
                registry.addViewController("/login").setViewName("/login.html");
                registry.addViewController("/swagger").setViewName("redirect:/swagger/index.html");
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addWebRequestInterceptor(oeiv());
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(BoardApplication.class, args);
    }

    @Autowired private EntityManagerFactory factory;

    @Bean
    public WebRequestInterceptor oeiv() {
        OpenEntityManagerInViewInterceptor oeiv = new OpenEntityManagerInViewInterceptor();
        oeiv.setEntityManagerFactory(factory);
        return oeiv;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
    }

}
