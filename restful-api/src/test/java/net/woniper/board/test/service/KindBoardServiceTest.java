package net.woniper.board.test.service;

import net.woniper.board.builder.EntityBuilder;
import net.woniper.board.domain.Board;
import net.woniper.board.domain.KindBoard;
import net.woniper.board.errors.support.KindBoardDuplicateException;
import net.woniper.board.errors.support.KindBoardNotFoundException;
import net.woniper.board.repository.KindBoardRepository;
import net.woniper.board.service.BoardService;
import net.woniper.board.service.KindBoardService;
import net.woniper.board.support.dto.BoardDto;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by woniper on 2015. 11. 5..
 */
public class KindBoardServiceTest extends BaseServiceTest {

    @Autowired private KindBoardService kindBoardService;
    @Autowired private KindBoardRepository kindBoardRepository;
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
    public void test_getKindBoard_id_notFoundException() throws Exception {
        // given
        Long kindBoardId = 0L;

        // when
        kindBoardService.getKindBoard(kindBoardId);

        // then
        fail("KindBoard NotFoundException");
    }

    @Test
    public void test_getKindBoard_id() throws Exception {
        // given
        Long kindBoardId = kindBoard.getKindBoardId();

        // when
        KindBoard getKindBoard = kindBoardService.getKindBoard(kindBoardId);

        // then
        assertEquals(kindBoard.getKindBoardName(), getKindBoard.getKindBoardName());
    }

    @Test(expected = KindBoardNotFoundException.class)
    public void test_getKindBoard_name_notFoundException() throws Exception {
        // given
        String kindBoardName = "";

        // when
        kindBoardService.getKindBoard(kindBoardName);

        // then
        fail("kindBoard NotFoundException");
    }

    @Test
    public void test_getKindBoard_name() throws Exception {
        // given
        String kindBoardName = kindBoard.getKindBoardName();

        // when
        KindBoard getKindBoard = kindBoardService.getKindBoard(kindBoardName);

        // then
        assertEquals(kindBoard.getKindBoardName(), getKindBoard.getKindBoardName());
    }

    @Test(expected = KindBoardNotFoundException.class)
    public void test_updateKindBoard_notFoundException() throws Exception {
        // given
        Long kindBoardId = 0L;
        String updateKindBoardName = "UPDATE";

        // when
        kindBoardService.updateKindBoard(kindBoardId, updateKindBoardName);

        // then
        fail("kindBoard NotFoundException");

    }

    @Test
    public void test_updateKindBoard() throws Exception {
        // given
        Long kindBoardId = kindBoard.getKindBoardId();
        String updateKindBoardName = "UPDATE";

        // when
        kindBoardService.updateKindBoard(kindBoardId, updateKindBoardName);

        // then
        assertEquals(updateKindBoardName, kindBoard.getKindBoardName());
    }

    @Test
    public void test_getKindBoardList() throws Exception {
        // given
        kindBoardRepository.deleteAll();
        kindBoardService.createKindBoard("ADD1");
        kindBoardService.createKindBoard("ADD2");
        kindBoardService.createKindBoard("ADD3");

        // when
        List<KindBoard> kindBoards = kindBoardService.getKindBoard();

        // then
        assertEquals(3, kindBoards.size());
    }

    @Test(expected = KindBoardDuplicateException.class)
    public void test_duplicateKindBoard() throws Exception {
        // given
        String kindBoardName = kindBoard.getKindBoardName();

        // when
        kindBoardService.createKindBoard(kindBoardName);

        // then
        fail("KindBoard DuplicateException");
    }
}
