package net.woniper.board.web.controller.test;

import net.woniper.board.BoardApplication;
import net.woniper.board.config.SecurityUserDetails;
import net.woniper.board.domain.User;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.service.UserService;
import net.woniper.board.web.builder.EntityBuilder;
import net.woniper.board.web.config.test.TestDatabaseConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by woniper on 15. 2. 4..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {BoardApplication.class, TestDatabaseConfig.class})
@WebAppConfiguration
@IntegrationTest("server.port=8888")
@Transactional
public class ViewControllerTest {

    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private Filter springSecurityFilterChain;
    @Autowired private UserService userService;

    private MockMvc mock;
    private User admin;
    private User user;

    @Before
    public void setUp() throws Exception {
        this.mock = webAppContextSetup(webApplicationContext).addFilter(springSecurityFilterChain).build();

        admin = userService.createUser(EntityBuilder.createUser(AuthorityType.ADMIN));
        user = userService.createUser(EntityBuilder.createUser(AuthorityType.USER));
    }

    @Test
    public void testSwaggerAdminRequest() throws Exception {
        ResultActions resultActions = mock.perform(get("/swagger").with(user(new SecurityUserDetails(admin))));
        resultActions.andDo(print()).andExpect(status().isFound());
    }

    @Test
    public void testSwaggerUserRequest() throws Exception {
        ResultActions resultActions = mock.perform(get("/swagger").with(user(new SecurityUserDetails(user))));
        resultActions.andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    public void testSwaggerNonUserRequest() throws Exception {
        ResultActions resultActions = mock.perform(get("/swagger"));
        resultActions.andDo(print()).andExpect(status().isUnauthorized());
    }
}