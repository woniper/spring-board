package net.woniper.board.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.woniper.board.BoardApplication;
import net.woniper.board.config.SecurityUserDetails;
import net.woniper.board.domain.Board;
import net.woniper.board.domain.User;
import net.woniper.board.repository.BoardRepository;
import net.woniper.board.service.impl.BoardServiceImpl;
import net.woniper.board.service.impl.UserServiceImpl;
import net.woniper.board.support.dto.BoardDto;
import net.woniper.board.support.dto.UserDto;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by woniper on 15. 1. 27..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port=8888")
@Transactional
public class BoardControllerTest {

    @Autowired private BoardRepository boardRepository;
    @Autowired private BoardServiceImpl boardService;
    @Autowired private UserServiceImpl userService;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ModelMapper modelMapper;
    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private Filter springSecurityFilterChain;

    private MockMvc mock;
    private Board board;
    private User adminUser;
    private String mediaType = MediaType.APPLICATION_JSON_VALUE;

    @Before
    public void setUp() throws Exception {
        this.mock = webAppContextSetup(webApplicationContext).addFilter(springSecurityFilterChain).build();

        adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword("12345");
        adminUser.setFirstName("kyung-won");
        adminUser.setLastName("lee");
        adminUser.setNickName("woniper");
        adminUser.setAdmin(true);
        adminUser = userService.createUser(modelMapper.map(adminUser, UserDto.Request.class));

        board = new Board();
        board.setTitle("testTitle");
        board.setContent("testContent");
        board.setUser(adminUser);
        board = boardRepository.save(board);

    }

    @Test
    public void test_게시글_생성() throws Exception {
        // given
        BoardDto newBoard = new BoardDto();
        newBoard.setTitle("newBoard");
        newBoard.setContent("newContent");

        // when
        ResultActions resultActions = mock.perform(post("/boards")
                .with(user(new SecurityUserDetails(adminUser)))
                .contentType(mediaType)
                .content(objectMapper.writeValueAsBytes(newBoard)));

        // thens
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(newBoard.getTitle())))
                .andExpect(jsonPath("$.content", is(newBoard.getContent())))
                .andExpect(jsonPath("$.userId", is(adminUser.getUserId().intValue())));
    }

    @Test
    public void test_게시글_생성_valid_check() throws Exception {
        // given
        Board newBoard = new Board();
        newBoard.setTitle("");  // 최소 2자 이상 입력
        newBoard.setContent("newContent");

        // when
        ResultActions resultActions = mock.perform(post("/boards")
                .with(user(new SecurityUserDetails(adminUser)))
                .contentType(mediaType)
                .content(objectMapper.writeValueAsBytes(newBoard)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    public void test_게시글_수정() throws Exception {
        // given
        board.setTitle("update Title");
        board.setContent("update Content");

        // when
        ResultActions resultActions = mock.perform(put("/boards")
                                            .with(user(new SecurityUserDetails(adminUser)))
                                            .contentType(mediaType)
                                            .content(objectMapper.writeValueAsBytes(board)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.boardId", is(board.getBoardId().intValue())))
                .andExpect(jsonPath("$.title", not("title")))
                .andExpect(jsonPath("$.title", is(board.getTitle())))
                .andExpect(jsonPath("$.content", not("content")))
                .andExpect(jsonPath("$.content", is(board.getContent())))
                .andExpect(jsonPath("$.readCount", is(board.getReadCount())));

    }

    @Test
    public void test_게시글_수정_권한_NOT_ACCEPTABLE() throws Exception {
        // given
        board.setTitle("update Title");
        board.setContent("update Content");
        User notAdminUser = createUser(false);

        // when
        ResultActions resultActions = mock.perform(put("/boards")
                                            .with(user(new SecurityUserDetails(notAdminUser)))
                                            .contentType(mediaType)
                                            .content(objectMapper.writeValueAsBytes(board)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void test_게시글_수정_권한_admin() throws Exception {
        // given
        User notAdminUser = createUser(false);
        Board newBoard = createBoard(notAdminUser);
        newBoard.setTitle("update Title");
        newBoard.setContent("update Content");

        // when
        ResultActions resultActions = mock.perform(put("/boards")
                                            .with(user(new SecurityUserDetails(adminUser)))
                                            .contentType(mediaType)
                                            .content(objectMapper.writeValueAsBytes(newBoard)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.boardId", is(newBoard.getBoardId().intValue())))
                .andExpect(jsonPath("$.title", is(newBoard.getTitle())))
                .andExpect(jsonPath("$.content", is(newBoard.getContent())))
                .andExpect(jsonPath("$.readCount", is(newBoard.getReadCount())))
                .andExpect(jsonPath("$.userId", is(notAdminUser.getUserId().intValue())));
    }

    @Test
    public void test_게시글_수정_valid_check() throws Exception {
        // todo
    }

    @Test
    public void test_게시글_삭제() throws Exception {
        // when
        ResultActions resultActions = mock.perform(delete("/board/" + board.getBoardId())
                                            .with(user(new SecurityUserDetails(adminUser)))
                                            .contentType(mediaType));

        //then
        resultActions.andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    public void test_게시글_삭제_권한_NOT_ACCEPTABLE() throws Exception {
        // given
        User notAdminUser = createUser(false);

        // when
        ResultActions resultActions = mock.perform(delete("/board/" + board.getBoardId())
                                            .with(user(new SecurityUserDetails(notAdminUser))));

        // then
        resultActions.andDo(print())
                .andExpect(status().isNotAcceptable());

    }

    @Test
    public void test_게시글_삭제_권한_admin() throws Exception {
        // given
        User notAdminUser = createUser(false);
        Board newBoard = createBoard(notAdminUser);

        // when
        ResultActions resultActions = mock.perform(delete("/board/" + newBoard.getBoardId())
                                            .with(user(new SecurityUserDetails(adminUser))));

        // then
        resultActions.andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    public void test_게시글_조회() throws Exception {
        // when
        ResultActions resultActions = mock.perform(get("/board/" + board.getBoardId())
                .with(user(new SecurityUserDetails(adminUser))));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boardId", is(board.getBoardId().intValue())))
                .andExpect(jsonPath("$.title", is(board.getTitle())))
                .andExpect(jsonPath("$.content", is(board.getContent())))
                .andExpect(jsonPath("$.readCount", is(board.getReadCount())))
                .andExpect(jsonPath("$.username", is(adminUser.getUsername())));
    }

    @Test
    public void test_게시글_리스트_조회_로그인() throws Exception {
        // given
        User newUser = createUser(true);
        List<Board> boardList = createBoardList(20, newUser)
                .stream().sorted((b1, b2) -> b2.getBoardId().compareTo(b1.getBoardId())).collect(Collectors.toList());

        // when
        ResultActions resultActions = mock.perform(get("/boards/1")
                                            .with(user(new SecurityUserDetails(adminUser))));

        // then
        resultActions.andDo(print()).andExpect(status().isOk());
        int count = boardList.size();

        for (int i = 0; i < count; i++) {
            Board newBoard = boardList.get(i);
            resultActions.andExpect(jsonPath("$.boards[" + i + "].boardId", is(newBoard.getBoardId().intValue())))
                            .andExpect(jsonPath("$.boards[" + i + "].title", is(newBoard.getTitle())))
                            .andExpect(jsonPath("$.boards[" + i + "].content", is(newBoard.getContent())))
                            .andExpect(jsonPath("$.boards[" + i + "].readCount", is(newBoard.getReadCount())))
                            .andExpect(jsonPath("$.boards[" + i + "].username", is(newUser.getUsername())));
        }
    }

    private User createUser(boolean isAdmin) {
        User newUser = new User();
        newUser.setUsername("newUsername");
        newUser.setPassword("newPassword");
        newUser.setFirstName("newUserFirstName");
        newUser.setLastName("newUserLastName");
        newUser.setNickName("newUserName");
        newUser.setAdmin(isAdmin);
        return userService.createUser(modelMapper.map(newUser, UserDto.Request.class));
    }


    private Board createBoard(User user) {
        Board newBoard = new Board();
        newBoard.setTitle("newBoard");
        newBoard.setContent("newContent");
        newBoard.setUser(user);
        return boardRepository.save(newBoard);
    }

    private List<Board> createBoardList(int size, User user) {
        List<Board> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Board newBoard = new Board();
            newBoard.setTitle("list title" + i);
            newBoard.setContent("list content" + i);
            newBoard.setUser(user);
            list.add(newBoard);
        }
        return boardRepository.save(list);
    }

    @Test
    public void test_bean() throws Exception {
        assertNotNull(boardRepository);
        assertNotNull(boardService);
        assertNotNull(objectMapper);
        assertNotNull(webApplicationContext);
    }
}
