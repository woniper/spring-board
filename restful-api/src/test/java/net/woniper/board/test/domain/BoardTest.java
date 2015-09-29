package net.woniper.board.test.domain;

import net.woniper.board.builder.EntityBuilder;
import net.woniper.board.domain.Board;
import net.woniper.board.domain.Comment;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by woniper on 15. 9. 25..
 */
public class BoardTest {

    Board board;

    @Before
    public void setUp() throws Exception {
        board = new Board("title", "content");
        Comment comment = new Comment();
        comment.setContent("comment");
        comment.setBoard(board);
    }

    @Test
    public void testRelational() throws Exception {
        // given
        Comment comment = board.getComments().get(0);

        // when
        Board board = comment.getBoard();

        // then
        assertEquals(this.board, board);
    }

    @Test
    public void testCommentCount() throws Exception {
        // given
        Comment comment = EntityBuilder.createComment(board);

        // when
        int commentCount = board.commentCount();

        // then
        assertEquals(2, commentCount);
    }
}
