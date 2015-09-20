package net.woniper.board.test.service;

import net.woniper.board.BoardApplication;
import net.woniper.board.builder.EntityBuilder;
import net.woniper.board.domain.Board;
import net.woniper.board.domain.Comment;
import net.woniper.board.domain.User;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.service.BoardService;
import net.woniper.board.service.CommentService;
import net.woniper.board.service.UserService;
import net.woniper.board.support.dto.BoardDto;
import net.woniper.board.support.dto.CommentDto;
import net.woniper.board.test.config.TestDatabaseConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by woniper on 15. 2. 4..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {BoardApplication.class, TestDatabaseConfig.class})
@WebAppConfiguration
@IntegrationTest("server.port=8888")
@Transactional
public class CommentServiceTest {

    @Autowired private UserService userService;
    @Autowired private BoardService boardService;
    @Autowired private CommentService commentService;

    @Autowired private ModelMapper modelMapper;

    private User admin;
    private User user;

    private Board adminBoard;
    private Board userBoard;

    private Comment adminComment;
    private Comment userComment;

    @Before
    public void setUp() throws Exception {
        admin = userService.createUser(EntityBuilder.createUser(AuthorityType.ADMIN));
        user = userService.createUser(EntityBuilder.createUser(AuthorityType.USER));

        adminBoard = boardService.createBoard
                (modelMapper.map(EntityBuilder.createBoard(admin), BoardDto.Response.class), admin.getUsername());
        userBoard = boardService.createBoard
                (modelMapper.map(EntityBuilder.createBoard(user), BoardDto.Response.class), user.getUsername());

        adminComment = commentService.createComment(modelMapper.map(EntityBuilder.createComment(adminBoard),
                CommentDto.class), adminBoard.getBoardId());

        userComment = commentService.createComment(modelMapper.map(EntityBuilder.createComment(userBoard),
                CommentDto.class), userBoard.getBoardId());
    }

    @Test
    public void test_createComment() throws Exception {
        // given
        Long boardId = adminBoard.getBoardId();
        CommentDto commentDto = modelMapper.map(EntityBuilder.createComment(adminBoard), CommentDto.class);

        // when
        Comment newComment = commentService.createComment(commentDto, boardId);

        // then
        assertEquals(commentDto.getContent(), newComment.getContent());
    }

    @Test
    public void test_deleteComment_admin이_user_comment_삭제() throws Exception {
        // given
        Long commentId = userComment.getCommentId();
        String username = admin.getUsername();

        // when
        boolean isDelete = commentService.deleteComment(commentId, username);

        // then
        assertEquals(true, isDelete);
    }

    @Test(expected = AccessDeniedException.class)
    public void test_deleteComment_user가_admin_comment_삭제() throws Exception {
        // given
        Long commentId = adminComment.getCommentId();
        String username = user.getUsername();

        // when
        commentService.deleteComment(commentId, username);

        // then
        fail("not delete");
    }

    @Test
    public void test_deleteComment_user() throws Exception {
        // given
        Long commentId = userComment.getCommentId();
        String username = user.getUsername();

        // when
        boolean isDelete = commentService.deleteComment(commentId, username);

        // then
        assertEquals(true, isDelete);
    }

    @Test
    public void test_deleteComment_admin() throws Exception {
        // given
        Long commentId = adminComment.getCommentId();
        String username = admin.getUsername();

        // when
        boolean isDelete = commentService.deleteComment(commentId, username);

        // then
        assertEquals(true, isDelete);
    }
}