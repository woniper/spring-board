package net.woniper.board.test.service;

import net.woniper.board.BoardApplication;
import net.woniper.board.repository.BoardRepository;
import net.woniper.board.repository.CommentRepository;
import net.woniper.board.repository.UserRepository;
import net.woniper.board.service.BoardService;
import net.woniper.board.service.CommentService;
import net.woniper.board.service.UserService;
import net.woniper.board.service.impl.BoardServiceImpl;
import net.woniper.board.service.impl.CommentServiceImpl;
import net.woniper.board.service.impl.UserServiceImpl;
import net.woniper.board.test.config.TestDatabaseConfig;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.assertNotNull;

/**
 * Created by woniper on 15. 2. 4..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {BoardApplication.class, TestDatabaseConfig.class})
@IntegrationTest("server.port=8888")
@WebAppConfiguration
@Transactional
public class BaseServiceTest {

    @Autowired protected ModelMapper modelMapper;

    @PersistenceContext EntityManager entityManager;

    @After
    public void after() throws Exception {
        entityManager.flush();
    }

    @Test
    public void testContextLoader() throws Exception {
        assertNotNull(modelMapper);
    }

    @Configuration
    public static class TestContextConfig {

        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }

        @Bean
        public BoardService boardService() {
            return new BoardServiceImpl();
        }

        @Bean
        public CommentService commentService() {
            return new CommentServiceImpl();
        }

        @Bean
        public UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);
        }

        @Bean
        public BoardRepository boardRepository() {
            return Mockito.mock(BoardRepository.class);
        }

        @Bean
        public CommentRepository commentRepository() {
            return Mockito.mock(CommentRepository.class);
        }
    }
}