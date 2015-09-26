package net.woniper.board.test.domain;

import net.woniper.board.domain.Board;
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
    }

    @Test
    public void testName() throws Exception {

    }
}
