package net.woniper.board.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by woniper on 15. 4. 8..
 */
@RestController
public class WebController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Map<String, String> test() {
        Map<String, String> map = new HashMap<>();
        map.put("title", "board");
        return map;
    }

}
