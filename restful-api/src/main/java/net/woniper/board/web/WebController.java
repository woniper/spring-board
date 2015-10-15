package net.woniper.board.web;

import net.woniper.board.component.FileUploaderImpl;
import net.woniper.board.domain.User;
import net.woniper.board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by woniper on 15. 4. 8..
 */
@RestController
public class WebController {

    @Autowired private UserService userService;
    @Autowired private FileUploaderImpl fileUploaderImpl;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Map<String, Object> test(Principal principal) {
        Map<String, Object> map = new HashMap<>();
        map.put("title", "board");

        User user = userService.getUser(principal.getName());
        if(user != null) {
            map.put("username", user.getUsername());
            map.put("authority", user.getAuthorityType());
        }
        return map;
    }

    @RequestMapping(value = "/web-session", method = RequestMethod.GET)
    public Map<String, Object> session(Principal principal) {
        Map<String, Object> map = new HashMap<>();
        if(principal != null) {
            User user = userService.getUser(principal.getName());
            map.put("session", true);
            map.put("username", user.getUsername());
            map.put("name", user.getLastName() + " " + user.getFirstName());
            map.put("nickName", user.getNickName());
            map.put("authorityType", user.getAuthorityType());
        } else {
            map.put("session", false);
        }

        return map;
    }

    @RequestMapping(value = "/uploads", method = RequestMethod.POST)
    public List<String> uploads(@RequestParam(value = "file") List<MultipartFile> files) {
        return fileUploaderImpl.uploads(files);
    }

}
