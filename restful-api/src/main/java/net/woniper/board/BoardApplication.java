package net.woniper.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.woniper.board.service.BoardService;
import net.woniper.board.service.UserService;
import net.woniper.board.support.dto.BoardDto;
import net.woniper.board.support.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
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
                registry.addViewController("/index").setViewName("index");
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addWebRequestInterceptor(oeiv());
            }
        };
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BoardApplication.class, args);

        // test context
        UserService userService = context.getBean(UserService.class);
        BoardService boardService = context.getBean(BoardService.class);
        UserDto.Request userDto = new UserDto.Request();
        userDto.setUsername("lkw1989");
        userDto.setPassword("12345");
        userDto.setFirstName("lee");
        userDto.setLastName("kw");
        userDto.setNickName("woniper");
        userDto.setAdmin(true);
        userService.createUser(userDto);

        for (int i = 0; i < 10; i++) {
            BoardDto boardDto = new BoardDto();
            boardDto.setTitle("test title" + i);
            boardDto.setContent("test content" + i);
            boardService.createBoard(boardDto, userDto.getUsername());
        }
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
