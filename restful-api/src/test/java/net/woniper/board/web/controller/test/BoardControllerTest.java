package net.woniper.board.web.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.woniper.board.BoardApplication;
import net.woniper.board.config.SecurityUserDetails;
import net.woniper.board.domain.Board;
import net.woniper.board.domain.User;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.repository.BoardRepository;
import net.woniper.board.service.BoardService;
import net.woniper.board.service.UserService;
import net.woniper.board.support.dto.BoardDto;
import net.woniper.board.web.builder.EntityBuilder;
import net.woniper.board.web.config.test.TestDatabaseConfig;
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
@SpringApplicationConfiguration(classes = {BoardApplication.class, TestDatabaseConfig.class})
@WebAppConfiguration
@IntegrationTest("server.port=8888")
@Transactional
public class BoardControllerTest {

    @Autowired private BoardRepository boardRepository;
    @Autowired private BoardService boardService;
    @Autowired private UserService userService;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ModelMapper modelMapper;
    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private Filter springSecurityFilterChain;

    private MockMvc mock;
    private Board board;
    private User admin;
    private User user;

    private String mediaType = MediaType.APPLICATION_JSON_VALUE;

    @Before
    public void setUp() throws Exception {
        this.mock = webAppContextSetup(webApplicationContext).addFilter(springSecurityFilterChain).build();

        admin = userService.createUser(EntityBuilder.createUser(AuthorityType.ADMIN));
        user = userService.createUser(EntityBuilder.createUser(AuthorityType.USER));
        board = boardRepository.save(EntityBuilder.createBoard(admin));
    }

    @Test
    public void test_게시글_생성() throws Exception {
        // given
        BoardDto newBoard = new BoardDto();
        newBoard.setTitle("newBoard");
        newBoard.setContent("newContent");

        // when
        ResultActions resultActions = mock.perform(post("/boards")
                .with(user(new SecurityUserDetails(admin)))
                .contentType(mediaType)
                .content(objectMapper.writeValueAsBytes(newBoard)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(newBoard.getTitle())))
                .andExpect(jsonPath("$.content", is(newBoard.getContent())))
                .andExpect(jsonPath("$.userId", is(admin.getUserId().intValue())));
    }

    @Test
    public void test_게시글_생성_valid_check() throws Exception {
        // given
        BoardDto newBoard = new BoardDto("", "newContent");   // title : 최소 2자 이상

        // when
        ResultActions resultActions = mock.perform(post("/boards")
                .with(user(new SecurityUserDetails(admin)))
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
        BoardDto boardDto = modelMapper.map(board, BoardDto.class);

        // when
        ResultActions resultActions = mock.perform(put("/boards/" + board.getBoardId())
                                            .with(user(new SecurityUserDetails(admin)))
                                            .contentType(mediaType)
                                            .content(objectMapper.writeValueAsBytes(boardDto)));

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
        BoardDto boardDto = modelMapper.map(board, BoardDto.class);

        // when
        ResultActions resultActions = mock.perform(put("/boards/" + board.getBoardId())
                                            .with(user(new SecurityUserDetails(user)))
                                            .contentType(mediaType)
                                            .content(objectMapper.writeValueAsBytes(boardDto)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void test_게시글_수정_권한_admin() throws Exception {
        // given
        Board newBoard = boardRepository.save(EntityBuilder.createBoard(user));
        newBoard.setTitle("update Title");
        newBoard.setContent("update Content");
        BoardDto boardDto = modelMapper.map(newBoard, BoardDto.class);

        // when
        ResultActions resultActions = mock.perform(put("/boards/" + newBoard.getBoardId())
                                            .with(user(new SecurityUserDetails(admin)))
                                            .contentType(mediaType)
                                            .content(objectMapper.writeValueAsBytes(boardDto)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.boardId", is(newBoard.getBoardId().intValue())))
                .andExpect(jsonPath("$.title", is(newBoard.getTitle())))
                .andExpect(jsonPath("$.content", is(newBoard.getContent())))
                .andExpect(jsonPath("$.readCount", is(newBoard.getReadCount())))
                .andExpect(jsonPath("$.userId", is(user.getUserId().intValue())));
    }

    @Test
    public void test_게시글_수정_valid_check() throws Exception {
        // todo
    }

    @Test
    public void test_게시글_삭제() throws Exception {
        // when
        ResultActions resultActions = mock.perform(delete("/boards/" + board.getBoardId())
                                            .with(user(new SecurityUserDetails(admin)))
                                            .contentType(mediaType));

        //then
        resultActions.andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    public void test_게시글_삭제_권한_NOT_ACCEPTABLE() throws Exception {
        // given
        // when
        ResultActions resultActions = mock.perform(delete("/boards/" + board.getBoardId())
                .contentType(mediaType)
                .with(user(new SecurityUserDetails(user))));

        // then
        resultActions.andDo(print())
                .andExpect(status().isNotAcceptable());

    }

    @Test
    public void test_게시글_삭제_권한_admin() throws Exception {
        // given
        Board newBoard = boardRepository.save(EntityBuilder.createBoard(user));

        // when
        ResultActions resultActions = mock.perform(delete("/boards/" + newBoard.getBoardId())
                .contentType(mediaType)
                .with(user(new SecurityUserDetails(admin))));

        // then
        resultActions.andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    public void test_게시글_조회() throws Exception {
        // when
        ResultActions resultActions = mock.perform(get("/boards/" + board.getBoardId())
                .contentType(mediaType)
                .with(user(new SecurityUserDetails(admin))));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boardId", is(board.getBoardId().intValue())))
                .andExpect(jsonPath("$.title", is(board.getTitle())))
                .andExpect(jsonPath("$.content", is(board.getContent())))
                .andExpect(jsonPath("$.readCount", is(board.getReadCount())))
                .andExpect(jsonPath("$.username", is(admin.getUsername())));
    }

    @Test
    public void test_게시글_리스트_조회_로그인() throws Exception {
        // given
//        User newUser = createUser(AuthorityType.ADMIN);
        createBoardList(20, admin);

        // when
        ResultActions resultActions = mock.perform(get("/boards?page=0&size=20")
                                            .contentType(mediaType)
                                            .with(user(new SecurityUserDetails(admin))));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk());
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
