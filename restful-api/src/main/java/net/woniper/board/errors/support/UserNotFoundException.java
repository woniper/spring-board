package net.woniper.board.errors.support;

import lombok.Getter;

/**
 * Created by woniper on 15. 9. 10..
 */
public class UserNotFoundException extends RuntimeException {

    @Getter private final String user;

    public UserNotFoundException(String user) {
        this.user = user;
    }
}
