package net.woniper.board;

import net.woniper.board.domain.Board;
import net.woniper.board.domain.KindBoard;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.repository.BoardRepository;
import net.woniper.board.repository.KindBoardRepository;
import net.woniper.board.service.BoardService;
import net.woniper.board.service.CommentService;
import net.woniper.board.service.UserService;
import net.woniper.board.support.dto.BoardDto;
import net.woniper.board.support.dto.CommentDto;
import net.woniper.board.support.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by woniper on 15. 9. 15..
 */
@Component
public class DatabaseLoader implements CommandLineRunner {

    @Autowired private UserService userService;
    @Autowired private BoardService boardService;
    @Autowired private CommentService commentService;
    @Autowired private KindBoardRepository kindBoardRepository;
    @Autowired private BoardRepository boardRepository;

    @Override
    public void run(String... args) throws Exception {
        // User
        UserDto.Request userDto = new UserDto.Request();
        userDto.setUsername("lkw1989");
        userDto.setPassword("12345");
        userDto.setFirstName("lee");
        userDto.setLastName("kw");
        userDto.setNickName("woniper");
        userDto.setAuthorityType(AuthorityType.ADMIN);
        userService.createUser(userDto);

        // KindBoard
        KindBoard kindBoard = kindBoardRepository.save(new KindBoard("일반 게시판"));
        kindBoardRepository.save(new KindBoard("Q&A"));

        for (int i = 0; i < 20; i++) {
            BoardDto.Request boardDto = new BoardDto.Request("test title" + i, "test content" + i);
            boardDto.setKindBoardName(kindBoard.getKindBoardName());
            Board board = boardService.createBoard(boardDto, userDto.getUsername());

            CommentDto commentDto = new CommentDto();
            commentDto.setContent("test comment content" + i);
            commentService.createComment(commentDto, board.getBoardId());
        }
    }
}
