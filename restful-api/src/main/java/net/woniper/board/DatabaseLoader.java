package net.woniper.board;

import net.woniper.board.domain.Board;
import net.woniper.board.domain.type.AuthorityType;
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

    @Override
    public void run(String... args) throws Exception {
        UserDto.Request userDto = new UserDto.Request();
        userDto.setUsername("lkw1989");
        userDto.setPassword("12345");
        userDto.setFirstName("lee");
        userDto.setLastName("kw");
        userDto.setNickName("woniper");
        userDto.setAuthorityType(AuthorityType.ADMIN);
        userService.createUser(userDto);

        for (int i = 0; i < 30; i++) {
            String username = userDto.getUsername();
            BoardDto.Request boardDto = new BoardDto.Request("test title" + i, "test content" + i);
            Board board = boardService.createBoard(boardDto, username);
            CommentDto commentDto = new CommentDto();
            commentDto.setContent("test comment content" + i);
            commentService.createComment(commentDto, board.getBoardId());
        }
    }
}
