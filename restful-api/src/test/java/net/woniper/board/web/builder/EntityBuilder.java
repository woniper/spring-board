package net.woniper.board.web.builder;

import net.woniper.board.domain.Board;
import net.woniper.board.domain.Comment;
import net.woniper.board.domain.User;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.support.dto.UserDto;
import org.modelmapper.ModelMapper;

/**
 * Created by woniper on 15. 8. 24..
 */
public class EntityBuilder {

    private final static ModelMapper modelMapper = new ModelMapper();

    public static UserDto.Request createUser(AuthorityType authorityType) {
        User user = new User();
        if(AuthorityType.ADMIN.equals(authorityType)) {
            user.setUsername("newAdminnName");
            user.setPassword("newAdminPassword");
            user.setFirstName("newAdminFirstName");
            user.setLastName("newAdminLastName");
            user.setNickName("newAdminName");
            user.setAuthorityType(authorityType);
        } else {
            user.setUsername("newUsername");
            user.setPassword("newPassword");
            user.setFirstName("newUserFirstName");
            user.setLastName("newUserLastName");
            user.setNickName("newUserName");
            user.setAuthorityType(authorityType);
        }
        return modelMapper.map(user, UserDto.Request.class);
    }

    public static Comment createComment(Board board) {
        Comment comment = new Comment();
        comment.setContent("comment content");
        comment.setBoard(board);
        comment.setDepth(0);
        return comment;
    }

    public static Board createBoard(User user) {
        Board board = new Board();
        board.setTitle("comment board title");
        board.setContent("comment board content");
        board.setUser(user);
        return board;
    }
}
