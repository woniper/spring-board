package net.woniper.board.test.controller;

import net.woniper.board.builder.EntityBuilder;
import net.woniper.board.config.SecurityUserDetails;
import net.woniper.board.domain.User;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by woniper on 15. 2. 4..
 */
public class ViewControllerTest extends BaseControllerTest {

    @Autowired private UserService userService;

    private User admin;
    private User user;

    @Before
    public void setUp() throws Exception {
        admin = userService.createUser(EntityBuilder.createUser(AuthorityType.ADMIN));
        user = userService.createUser(EntityBuilder.createUser(AuthorityType.USER));
    }

    @Test
    public void testSwaggerAdminRequest() throws Exception {
        ResultActions resultActions = mock.perform(get("/swagger/index.html")
                .with(user(new SecurityUserDetails(admin))));
        resultActions.andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void testSwaggerUserRequest() throws Exception {
        ResultActions resultActions = mock.perform(get("/swagger/index.html").with(user(new SecurityUserDetails(user))));
        resultActions.andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    public void testSwaggerNoneUserRequest() throws Exception {
        ResultActions resultActions = mock.perform(get("/swagger/index.html"));
        resultActions.andDo(print()).andExpect(status().isFound());
    }
}