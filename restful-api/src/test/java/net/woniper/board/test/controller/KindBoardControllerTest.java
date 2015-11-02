package net.woniper.board.test.controller;

import net.woniper.board.builder.EntityBuilder;
import net.woniper.board.config.SecurityUserDetails;
import net.woniper.board.domain.KindBoard;
import net.woniper.board.domain.User;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.service.KindBoardService;
import net.woniper.board.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by woniper on 15. 2. 4..
 */
public class KindBoardControllerTest extends BaseControllerTest {

    @Autowired private UserService userService;
    @Autowired private KindBoardService kindBoardService;

    private KindBoard kindBoard;
    private User admin;
    private User user;

    @Before
    public void setUp() throws Exception {
        this.admin = userService.createUser(EntityBuilder.createUser(AuthorityType.ADMIN));
        this.user = userService.createUser(EntityBuilder.createUser(AuthorityType.USER));
        this.kindBoard = kindBoardService.createKindBoard("ABC");
    }

    @Test
    public void test_조회_admin() throws Exception {
        // given

        // when
        ResultActions result = mock.perform(get("/kindBoards/" + kindBoard.getKindBoardId().intValue())
                .with(user(new SecurityUserDetails(admin))));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kindBoardName", is(kindBoard.getKindBoardName())));
    }
}