package demo.binder.test;

import demo.binder.Level;
import demo.binder.LevelPropertyEditor;
import org.junit.Test;
import org.springframework.web.bind.WebDataBinder;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by woniper on 15. 4. 28..
 */
public class LevelBinderTest {

    @Test
    public void levelPropertyEditorTest() {
        WebDataBinder dataBinder = new WebDataBinder(null);
        dataBinder.registerCustomEditor(Level.class, new LevelPropertyEditor());
        assertThat(dataBinder.convertIfNecessary("1", Level.class), is(Level.BASIC));
    }
}
