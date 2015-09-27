package net.woniper.board.domain.type;

import java.beans.PropertyEditorSupport;

/**
 * Created by woniper on 15. 8. 22..
 */
public enum AuthorityType {
    ADMIN, USER;

    public static class AuthorityTypeProperty extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            setValue(text.toUpperCase());
        }
    }
}
