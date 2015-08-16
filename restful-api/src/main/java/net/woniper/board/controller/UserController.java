package net.woniper.board.controller;

import net.woniper.board.domain.User;
import net.woniper.board.service.BoardService;
import net.woniper.board.service.UserService;
import net.woniper.board.support.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;

/**
 * Created by woniper on 15. 1. 28..
 */
@RestController
public class UserController {

    @Autowired private UserService userService;
    @Autowired private BoardService boardService;
    @Autowired private ModelMapper modelMapper;

    @RequestMapping(value = "/users", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity createNewUser(@RequestBody @Valid UserDto.Request userDto, BindingResult result) {

        if(result.hasErrors()) {
            return new ResponseEntity<> (result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        User newUser = userService.createUser(userDto);
        return new ResponseEntity<> (modelMapper.map(newUser, UserDto.Response.class), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateUser(@RequestBody @Valid UserDto.Request userDto, BindingResult result, Principal principal) {
        if(result.hasErrors()) {
            return new ResponseEntity<> (result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        User updateUser = userService.updateUser(userDto, principal.getName());
        if(updateUser != null)
            return new ResponseEntity<> (modelMapper.map(updateUser, UserDto.Response.class), HttpStatus.ACCEPTED);

        return new ResponseEntity<> (HttpStatus.NOT_ACCEPTABLE);
    }

    @RequestMapping(value = "/users", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(Principal principal) {
        if(userService.deleteUser(principal.getName()))
            return new ResponseEntity<> (HttpStatus.ACCEPTED);

        return new ResponseEntity<> (HttpStatus.BAD_REQUEST);
    }

    /**
     * @param page
     * @param limit
     * @param orderBy
     * @param orderDir
     * @param principal
     * @return
     * @throws UserPrincipalNotFoundException
     */
    @RequestMapping(value = "/users/boards/{page}", method = RequestMethod.GET)
    public ResponseEntity getUserBoardList(@PathVariable("page") int page,
                                           @RequestParam(value = "limit", required = false, defaultValue = "20") int limit,
                                           @RequestParam(value = "orderBy", required = false, defaultValue = "boardId") String orderBy,
                                           @RequestParam(value = "orderDir", required = false, defaultValue = "DESC") String orderDir,
                                           Principal principal) throws UserPrincipalNotFoundException {
        User user = userService.getUser(principal.getName());

        if (user == null) {
            throw new UserPrincipalNotFoundException(principal.getName());
        }

        return new ResponseEntity<> (boardService.getUserBoardList(user.getUserId(), page, limit, orderBy, orderDir, UserController.class), HttpStatus.OK);

    }
}
