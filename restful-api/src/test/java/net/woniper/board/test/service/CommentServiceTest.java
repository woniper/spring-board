package net.woniper.board.test.service;

import net.woniper.board.builder.EntityBuilder;
import net.woniper.board.domain.Board;
import net.woniper.board.domain.Comment;
import net.woniper.board.domain.KindBoard;
import net.woniper.board.service.BoardService;
import net.woniper.board.service.CommentService;
import net.woniper.board.service.KindBoardService;
import net.woniper.board.support.dto.BoardDto;
import net.woniper.board.support.dto.CommentDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by woniper on 15. 2. 4..
 */
public class CommentServiceTest extends BaseServiceTest {

    @Autowired private BoardService boardService;
    @Autowired private CommentService commentService;
    @Autowired private KindBoardService kindBoardService;

    private Board adminBoard;
    private Board userBoard;

    private Comment adminComment;
    private Comment userComment;

    private KindBoard kindBoard;

    @Before
    public void setUp() throws Exception {
        kindBoard = kindBoardService.createKindBoard("General");
        Board newAdminBoard = EntityBuilder.createBoard(admin);
        newAdminBoard.setKindBoard(kindBoard);

        Board newUserBoard = EntityBuilder.createBoard(user);
        newUserBoard.setKindBoard(kindBoard);

        adminBoard = boardService.createBoard(modelMapper.map(newAdminBoard, BoardDto.Request.class), admin.getUsername());
        userBoard = boardService.createBoard(modelMapper.map(newUserBoard, BoardDto.Request.class), user.getUsername());

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