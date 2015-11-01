package net.woniper.board.errors.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by woniper on 2015. 10. 30..
 */
@AllArgsConstructor
public class KindBoardNotFoundException extends RuntimeException {
    @Getter private String kindBoard;
}
