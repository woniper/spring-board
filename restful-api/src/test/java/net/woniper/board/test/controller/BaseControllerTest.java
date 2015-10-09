package net.woniper.board.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.woniper.board.BoardApplication;
import net.woniper.board.test.config.TestDatabaseConfig;
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

    protected MockMvc mock;
    protected String mediaType = MediaType.APPLICATION_JSON_VALUE;

    @Before
    public void baseSetUp() throws Exception {
        this.mock = webAppContextSetup(webApplicationContext).addFilter(springSecurityFilterChain).build();
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
