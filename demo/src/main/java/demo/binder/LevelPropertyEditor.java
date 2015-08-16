package demo.binder;

import java.beans.PropertyEditorSupport;

/**
 * Created by woniper on 15. 4. 28..
 */
public class LevelPropertyEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        return String.valueOf(((Level) this.getValue()).intValue());
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        this.setValue(Level.valueOf(Integer.parseInt(text)));
    }
}
