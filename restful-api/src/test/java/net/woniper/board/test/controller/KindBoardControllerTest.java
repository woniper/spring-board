package net.woniper.board.test.controller;

import net.woniper.board.config.SecurityUserDetails;
import net.woniper.board.domain.KindBoard;
import net.woniper.board.service.KindBoardService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by woniper on 15. 2. 4..
 */
public class KindBoardControllerTest extends BaseControllerTest {

    @Autowired private KindBoardService kindBoardService;

    private KindBoard kindBoard;

    @Before
    public void setUp() throws Exception {
        this.kindBoard = kindBoardService.createKindBoard("ABC");
    }

    @Test
    public void test_조회_admin() throws Exception {
        // when
        ResultActions result = mock.perform(get("/kindBoards/" + kindBoard.getKindBoardId().intValue())
                .with(user(new SecurityUserDetails(admin))));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kindBoardName", is(kindBoard.getKindBoardName())));
    }

    @Test
    public void test_조회_user() throws Exception {
        // when
        ResultActions result = mock.perform(get("/kindBoards/" + kindBoard.getKindBoardId().intValue())
                .with(user(new SecurityUserDetails(user))));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kindBoardName", is(kindBoard.getKindBoardName())));
    }

    @Test
    public void test_등록_admin() throws Exception {
        // given
        String kindBoardName = "TEST";

        // when
        ResultActions result = mock.perform(post("/kindBoards?kindBoardName=" + kindBoardName)
                .with(user(new SecurityUserDetails(admin))));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kindBoardName", is(kindBoardName)));
    }

    @Test
    public void test_등록_user() throws Exception {
        // given
        String kindBoardName = "TEST";

        // when
        ResultActions result = mock.perform(post("/kindBoards?kindBoardName=" + kindBoardName)
                .with(user(new SecurityUserDetails(user))));

        // then
        result.andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void test_변경_admin() throws Exception {
        // given
        String updateKindBoardName = "UPDATE";

        // when
        ResultActions result = mock.perform(put("/kindBoards/" + kindBoard.getKindBoardId().intValue() +
                "?kindBoardName=" + updateKindBoardName)
                .with(user(new SecurityUserDetails(admin))));

        // then
        result.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void test_변경_user() throws Exception {
        // given
        String updateKindBoardName = "UPDATE";

        // when
        ResultActions result = mock.perform(put("/kindBoards/" + kindBoard.getKindBoardId().intValue() +
                "?kindBoardName=" + updateKindBoardName)
                .with(user(new SecurityUserDetails(user))));

        // then
        result.andDo(print()).andExpect(status().isForbidden());
    }
}