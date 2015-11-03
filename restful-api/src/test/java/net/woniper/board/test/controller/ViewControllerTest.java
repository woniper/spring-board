package net.woniper.board.test.controller;

import net.woniper.board.config.SecurityUserDetails;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by woniper on 15. 2. 4..
 */
public class ViewControllerTest extends BaseControllerTest {

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