package net.woniper.board.test.service;

import net.woniper.board.builder.EntityBuilder;
import net.woniper.board.domain.Board;
import net.woniper.board.domain.KindBoard;
import net.woniper.board.errors.support.KindBoardNotFoundException;
import net.woniper.board.service.BoardService;
import net.woniper.board.service.KindBoardService;
import net.woniper.board.support.dto.BoardDto;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by woniper on 2015. 11. 5..
 */
public class KindBoardServiceTest extends BaseServiceTest {

    @Autowired private KindBoardService kindBoardService;
    @Autowired private BoardService boardService;
    @Autowired private ModelMapper modelMapper;

    private Board board;
    private KindBoard kindBoard;

    @Before
    public void setUp() throws Exception {
        this.kindBoard = kindBoardService.createKindBoard("TEST");
        BoardDto.Request boardDto = modelMapper.map(EntityBuilder.createBoard(admin), BoardDto.Request.class);
        boardDto.setKindBoardName(kindBoard.getKindBoardName());
        this.board = boardService.createBoard(boardDto, admin.getUsername());
    }

    @Test(expected = KindBoardNotFoundException.class)
    public void test_getKindBoard_notFoundException() throws Exception {
        // given
        Long kindBoardId = 0L;

        // when
        kindBoardService.getKindBoard(kindBoardId);

        // then
        fail("KindBoard NotFoundException");
    }

    @Test
    public void test_getKindBoard() throws Exception {
        // given
        Long kindBoardId = kindBoard.getKindBoardId();

        // when
        KindBoard getKindBoard = kindBoardService.getKindBoard(kindBoardId);

        // then
        assertEquals(kindBoard.getKindBoardName(), getKindBoard.getKindBoardName());
    }
}
