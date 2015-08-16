package demo.binder;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by woniper on 15. 4. 28..
 */
@RestController
public class BinderController {

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(Level.class, new LevelPropertyEditor());
    }

    @RequestMapping(value = "/binder", method = RequestMethod.GET)
    public String binder(@RequestParam("level") Level level) {
        return level.intValue() + "";
    }

}
