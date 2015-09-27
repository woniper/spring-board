package net.woniper.board.support.type;

import java.beans.PropertyEditorSupport;

/**
 * Created by woniper on 15. 9. 24..
 */
public enum SearchType {

    WRITER, TITLE, CONTENT, ALL;

    public static class SearchTypeProperty extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            setValue(text.toUpperCase());
        }
    }

}
