package net.woniper.board.errors.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by woniper on 15. 9. 19..
 */
@AllArgsConstructor
public class CommentNotFoundException extends RuntimeException {

    @Getter private Long commentId;

}
