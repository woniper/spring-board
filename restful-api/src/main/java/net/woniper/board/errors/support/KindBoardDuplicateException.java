package net.woniper.board.errors.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by woniper on 2015. 11. 8..
 */
@AllArgsConstructor
public class KindBoardDuplicateException extends RuntimeException {
    @Getter String kindBoardName;
}
