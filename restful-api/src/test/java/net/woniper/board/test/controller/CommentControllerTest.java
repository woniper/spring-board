package net.woniper.board.test.controller;

import net.woniper.board.builder.EntityBuilder;
import net.woniper.board.config.SecurityUserDetails;
import net.woniper.board.domain.Board;
import net.woniper.board.domain.Comment;
import net.woniper.board.domain.KindBoard;
import net.woniper.board.repository.BoardRepository;
import net.woniper.board.repository.CommentRepository;
import net.woniper.board.service.KindBoardService;
import net.woniper.board.support.dto.CommentDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by woniper on 15. 2. 4..
 */
public class CommentControllerTest extends BaseControllerTest {

    @Autowired private BoardRepository boardRepository;
    @Autowired private CommentRepository commentRepository;
    @Autowired private KindBoardService kindBoardService;

    // fixture data
    private Board adminBoard;
    private Board userBoard;
    private Comment adminComment;
    private Comment userComment;
    private KindBoard kindBoard;

    @Before
    public void setUp() throws Exception {
        kindBoard = kindBoardService.save("General");
        Board newAdminBoard = EntityBuilder.createBoard(admin);
        newAdminBoard.setKindBoard(kindBoard);
        Board newUserBoard = EntityBuilder.createBoard(user);
        newUserBoard.setKindBoard(kindBoard);
        adminBoard = boardRepository.save(newAdminBoard);
        userBoard = boardRepository.save(newUserBoard);
        adminComment = commentRepository.save(EntityBuilder.createComment(adminBoard));
        userComment = commentRepository.save(EntityBuilder.createComment(userBoard));
    }

    @Test
    public void test_댓글_등록() throws Exception {
        // given
        CommentDto commentDto = new CommentDto();
        commentDto.setContent("new comment content");

        // when
        ResultActions resultActions = mock.perform(post("/boards/" + adminBoard.getBoardId() + "/comments")
                .with(user(new SecurityUserDetails(admin)))
                .contentType(mediaType)
                .content(objectMapper.writeValueAsString(commentDto)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content", is(commentDto.getContent())))
                .andExpect(jsonPath("$.username", is(admin.getUsername())));
    }

    @Test
    public void test_댓글_조회() throws Exception {
        // given
        // when
        ResultActions resultActions = mock.perform(get("/boards/" + adminBoard.getBoardId())
                .contentType(mediaType)
                .with(user(new SecurityUserDetails(admin))));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boardId", is(adminBoard.getBoardId().intValue())))
                .andExpect(jsonPath("$.title", is(adminBoard.getTitle())))
                .andExpect(jsonPath("$.comments[0].commentId", is(adminBoard.getComments().get(0).getCommentId().intValue())))
                .andExpect(jsonPath("$.comments[0].content", is(adminBoard.getComments().get(0).getContent())))
                .andExpect(jsonPath("$.userId", is(admin.getUserId().intValue())))
                .andExpect(jsonPath("$.username", is(admin.getUsername())));
    }

    @Test
    public void test_댓글_삭제_admin_isAccepted() throws Exception {
        // given
        // when
        ResultActions resultActions = mock.perform(delete("/boards/comments/" + userComment.getCommentId())
                .contentType(mediaType)
                .with(user(new SecurityUserDetails(admin))));

        // then
        resultActions.andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    public void test_댓글_삭제_user_isAccepted() throws Exception {
        // given
        // when
        ResultActions resultActions = mock.perform(delete("/boards/comments/" + userComment.getCommentId())
                .contentType(mediaType)
                .with(user(new SecurityUserDetails(user))));

        // then
        resultActions.andDo(print())
                .andExpect(status().isAccepted());
    }
    @Test
    public void test_댓글_삭제_user_isNotAccepted() throws Exception {
        // given
        // when
        ResultActions resultActions = mock.perform(delete("/boards/comments/" + adminComment.getCommentId())
                .contentType(mediaType)
                .with(user(new SecurityUserDetails(user))));

        // then
        resultActions.andDo(print())
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void test_댓글_수정_admin_isAccepted() throws Exception {
        // given
        userComment.setContent("updateUserContent");


        // when
        ResultActions resultActions = mock.perform(put("/boards/comments/" + userComment.getCommentId())
                .contentType(mediaType)
                .content(objectMapper.writeValueAsBytes(modelMapper.map(userComment, CommentDto.class)))
                .with(user(new SecurityUserDetails(admin))));

        // then
        resultActions.andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.content", is(userComment.getContent())))
                .andExpect(jsonPath("$.username", is(user.getUsername())));
    }

    @Test
    public void test_댓글_수정_user_isAccepted() throws Exception {
        // given
        userComment.setContent("updateUserContent");

        // when
        ResultActions resultActions = mock.perform(put("/boards/comments/" + userComment.getCommentId())
                .contentType(mediaType)
                .content(objectMapper.writeValueAsBytes(modelMapper.map(userComment, CommentDto.class)))
                .with(user(new SecurityUserDetails(user))));

        // then
        resultActions.andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.content", is(userComment.getContent())))
                .andExpect(jsonPath("$.username", is(user.getUsername())));
    }

    @Test
    public void test_댓글_수정_user_isNotAccepted() throws Exception {
        // given
        adminComment.setContent("updateUserContent");

        // when
        ResultActions resultActions = mock.perform(put("/boards/comments/" + adminComment.getCommentId())
                .contentType(mediaType)
                .content(objectMapper.writeValueAsBytes(modelMapper.map(adminComment, CommentDto.class)))
                .with(user(new SecurityUserDetails(user))));

        // then
        resultActions.andDo(print())
                .andExpect(status().isNotAcceptable());
    }
}