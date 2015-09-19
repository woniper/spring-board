package net.woniper.board.errors.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by woniper on 15. 9. 15..
 */
@AllArgsConstructor
public class BoardNotFoundException extends RuntimeException {

    @Getter private Long boardId;

}