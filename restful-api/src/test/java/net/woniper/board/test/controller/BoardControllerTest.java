package net.woniper.board.test.controller;

import net.woniper.board.builder.EntityBuilder;
import net.woniper.board.config.SecurityUserDetails;
import net.woniper.board.domain.Board;
import net.woniper.board.domain.User;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.repository.BoardRepository;
import net.woniper.board.service.BoardService;
import net.woniper.board.service.UserService;
import net.woniper.board.support.dto.BoardDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by woniper on 15. 1. 27..
 */
public class BoardControllerTest extends BaseControllerTest {

    @Autowired private BoardRepository boardRepository;
    @Autowired private BoardService boardService;
    @Autowired private UserService userService;

    private Board board;
    private User admin;
    private User user;

    @Before
    public void setUp() throws Exception {
        admin = userService.createUser(EntityBuilder.createUser(AuthorityType.ADMIN));
        user = userService.createUser(EntityBuilder.createUser(AuthorityType.USER));
        board = boardRepository.save(EntityBuilder.createBoard(admin));
    }

    @Test
    public void test_게시글_생성() throws Exception {
        // given
        BoardDto newBoard = new BoardDto("newBoard", "newContent");
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
        equalsResultDataAndThen(resultActions, status().isAccepted(), board, admin);
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
                .andExpect(status().isNotFound());
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
        equalsResultDataAndThen(resultActions, status().isAccepted(), newBoard, user);
    }

    @Test
    public void test_게시글_수정_valid_check() throws Exception {
        // todo
    }

    @Test
    public void test_게시글_삭제() throws Exception {
        // when
        ResultActions resultActions = mock.perform(delete("/boards/" + board.getBoardId())
                                            .with(user(new SecurityUserDetails(admin))));

        //then
        resultActions.andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    public void test_게시글_삭제_권한_NOT_ACCEPTABLE() throws Exception {
        // given
        // when
        ResultActions resultActions = mock.perform(delete("/boards/" + board.getBoardId())
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
                .with(user(new SecurityUserDetails(admin))));

        // then
        resultActions.andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    public void test_게시글_조회() throws Exception {
        // when
        ResultActions resultActions = mock.perform(get("/boards/" + board.getBoardId())
                .with(user(new SecurityUserDetails(admin))));

        // then
        equalsResultDataAndThen(resultActions, status().isOk(), board, admin);
    }

    @Test
    public void test_게시글_리스트_조회_로그인() throws Exception {
        // given
        createBoardList(20, admin);

        // when
        ResultActions resultActions = mock.perform(get("/boards?page=0&size=20")
                                            .with(user(new SecurityUserDetails(admin))));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk());
    }

    private void equalsResultDataAndThen(ResultActions resultActions, ResultMatcher resultMatcher, Board board, User user) throws Exception {
        resultActions.andDo(print())
                .andExpect(resultMatcher)
                .andExpect(jsonPath("$.boardId", is(board.getBoardId().intValue())))
                .andExpect(jsonPath("$.title", is(board.getTitle())))
                .andExpect(jsonPath("$.content", is(board.getContent())))
                .andExpect(jsonPath("$.readCount", is(board.getReadCount())))
                .andExpect(jsonPath("$.username", is(user.getUsername())));

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
    public void test_getBoard_BoardNotFoundException() throws Exception {
        // given
        Long boardId = Long.MAX_VALUE;
        HttpStatus status = HttpStatus.NOT_FOUND;

        // when
        ResultActions resultActions = mock.perform(get("/boards/" + boardId.intValue())
                    .with(user(new SecurityUserDetails(admin))));

        // then
        resultActions.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("status", is(status.value())))
                .andExpect(jsonPath("message", is(status.getReasonPhrase())));
    }

    @Test
    public void test_bean() throws Exception {
        assertNotNull(boardRepository);
        assertNotNull(boardService);
        assertNotNull(objectMapper);
        assertNotNull(webApplicationContext);
    }
}
