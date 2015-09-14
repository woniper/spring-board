package net.woniper.board.test.service;

import net.woniper.board.BoardApplication;
import net.woniper.board.builder.EntityBuilder;
import net.woniper.board.domain.Board;
import net.woniper.board.domain.User;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.errors.support.UserNotFoundException;
import net.woniper.board.service.BoardService;
import net.woniper.board.service.UserService;
import net.woniper.board.support.dto.BoardDto;
import net.woniper.board.test.config.TestDatabaseConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
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
public class BoardServiceTest {

    @Autowired private UserService userService;
    @Autowired private BoardService boardService;

    @Autowired private ModelMapper modelMapper;

    private User admin;
    private User user;

    private Board adminBoard;
    private Board userBoard;

    @Before
    public void setUp() throws Exception {
        admin = userService.createUser(EntityBuilder.createUser(AuthorityType.ADMIN));
        user = userService.createUser(EntityBuilder.createUser(AuthorityType.USER));

        adminBoard = boardService.createBoard
                (modelMapper.map(EntityBuilder.createBoard(admin), BoardDto.Response.class), admin.getUsername());
        userBoard = boardService.createBoard
                (modelMapper.map(EntityBuilder.createBoard(user), BoardDto.Response.class), user.getUsername());
    }

    @Test
    public void test_getBoard() throws Exception {
        // given
        assertEquals(0, userBoard.getReadCount());

        // when
        Board board = boardService.getBoard(userBoard.getBoardId());

        // then
        assertEquals(1, board.getReadCount());
        assertEquals(userBoard.getBoardId(), board.getBoardId());
        assertEquals(userBoard.getTitle(), board.getTitle());
    }

    @Test(expected = UserNotFoundException.class)
    public void test_createBoard_UserNotFound() throws Exception {
        // given
        BoardDto boardDto = modelMapper.map(EntityBuilder.createBoard(user), BoardDto.class);

        // when
        boardService.createBoard(boardDto, "notUser");

        // then
        fail("UserNotFoundException");

    }
}