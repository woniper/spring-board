package net.woniper.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.woniper.board.domain.Board;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.service.BoardService;
import net.woniper.board.service.CommentService;
import net.woniper.board.service.UserService;
import net.woniper.board.support.dto.BoardDto;
import net.woniper.board.support.dto.CommentDto;
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
import org.springframework.web.servlet.config.annotation.*;

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
        ConfigurableApplicationContext context = SpringApplication.run(BoardApplication.class, args);

        // test context
        UserService userService = context.getBean(UserService.class);
        BoardService boardService = context.getBean(BoardService.class);
        CommentService commentService = context.getBean(CommentService.class);

        UserDto.Request userDto = new UserDto.Request();
        userDto.setUsername("lkw1989");
        userDto.setPassword("12345");
        userDto.setFirstName("lee");
        userDto.setLastName("kw");
        userDto.setNickName("woniper");
        userDto.setAuthorityType(AuthorityType.ADMIN);
        userService.createUser(userDto);

        for (int i = 0; i < 10; i++) {
            String username = userDto.getUsername();
            BoardDto boardDto = new BoardDto();
            boardDto.setTitle("test title" + i);
            boardDto.setContent("test content" + i);
            Board board = boardService.createBoard(boardDto, username);
            CommentDto commentDto = new CommentDto();
            commentDto.setContent("test comment content" + i);
            commentService.createComment(commentDto, board.getBoardId(), username);
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
