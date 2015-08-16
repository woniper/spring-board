package demo.inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by woniper on 15. 5. 2..
 */
@RestController
public class InjectController {

    @Autowired @Qualifier(value = "autoInject") Inject inject;

    @RequestMapping(value = "/inject", method = RequestMethod.GET)
    public void inject() {
    }

}
