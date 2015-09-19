package net.woniper.board.errors.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by woniper on 15. 9. 10..
 */
@AllArgsConstructor
public class UserNotFoundException extends RuntimeException {

    @Getter private final String user;

}
