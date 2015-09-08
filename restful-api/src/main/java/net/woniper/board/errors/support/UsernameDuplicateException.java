package net.woniper.board.errors.support;

import lombok.Getter;

/**
 * Created by woniper on 15. 2. 9..
 */
public class UsernameDuplicateException extends RuntimeException {

    @Getter private final String username;

    public UsernameDuplicateException(String username) {
        this.username = username;
    }
}
