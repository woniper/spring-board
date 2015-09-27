package net.woniper.board.test.domain;

import net.woniper.board.domain.Board;
import net.woniper.board.domain.Comment;
import org.junit.Before;
import org.junit.Test;

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
    public void testName() throws Exception {

    }
}
