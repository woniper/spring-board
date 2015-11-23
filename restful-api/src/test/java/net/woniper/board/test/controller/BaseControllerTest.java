package net.woniper.board.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.woniper.board.BoardApplication;
import net.woniper.board.builder.EntityBuilder;
import net.woniper.board.domain.User;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.service.UserService;
import net.woniper.board.test.config.TestDatabaseConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.Filter;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by woniper on 15. 1. 27..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {BoardApplication.class, TestDatabaseConfig.class})
@WebAppConfiguration
@IntegrationTest("server.port=8888")
@Transactional
public class BaseControllerTest {

    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected ModelMapper modelMapper;
    @Autowired protected WebApplicationContext webApplicationContext;
    @Autowired protected Filter springSecurityFilterChain;
    @Autowired protected UserService userService;

    @PersistenceContext EntityManager entityManager;

    protected MockMvc mock;
    protected String mediaType = MediaType.APPLICATION_JSON_VALUE;

    protected User admin;
    protected User user;

    @Before
    public void baseSetUp() throws Exception {
        this.mock = webAppContextSetup(webApplicationContext).addFilter(springSecurityFilterChain).build();
        this.admin = userService.save(EntityBuilder.createUser(AuthorityType.ADMIN));
        this.user = userService.save(EntityBuilder.createUser(AuthorityType.USER));
    }

    @After
    public void after() throws Exception {
        entityManager.flush();
    }

    @Test
    public void testContextLoader() throws Exception {
        assertNotNull(objectMapper);
        assertNotNull(modelMapper);
        assertNotNull(webApplicationContext);
        assertNotNull(springSecurityFilterChain);
        assertNotNull(mock);
        assertNotNull(mediaType);
    }
}
