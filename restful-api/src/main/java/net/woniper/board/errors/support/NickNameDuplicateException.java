package net.woniper.board.errors.support;

import lombok.Getter;

/**
 * Created by woniper on 15. 2. 9..
 */
public class NickNameDuplicateException extends RuntimeException {

    @Getter private final String nickName;

    public NickNameDuplicateException(String nickName) {
        this.nickName = nickName;
    }
}
