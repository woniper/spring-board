package net.woniper.board.web.controller;

import net.woniper.board.domain.Board;
import net.woniper.board.domain.User;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.service.BoardService;
import net.woniper.board.service.UserService;
import net.woniper.board.support.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Created by woniper on 15. 1. 28..
 */
@RestController
@RequestMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired private UserService userService;
    @Autowired private BoardService boardService;
    @Autowired private ModelMapper modelMapper;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(AuthorityType.class, new AuthorityType.AuthorityTypeProperty());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createNewUser(@RequestBody @Valid UserDto.Request userDto, BindingResult result) {

        if(result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        User newUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(newUser, UserDto.Response.class));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserDto.Request userDto, BindingResult result, Principal principal) {
        if(result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        User updateUser = userService.updateUser(userDto, principal.getName());
        if(updateUser != null)
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(modelMapper.map(updateUser, UserDto.Response.class));

        return new ResponseEntity<> (HttpStatus.NOT_ACCEPTABLE);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(Principal principal) {
        if(userService.deleteUser(principal.getName()))
            return new ResponseEntity<> (HttpStatus.ACCEPTED);

        return new ResponseEntity<> (HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/boards", method = RequestMethod.GET)
    public ResponseEntity<?> getUserBoardList(Pageable pageable, Principal principal) {
        Page<Board> boards = boardService.getBoard(pageable, principal.getName());

        if(boards != null) {
            return ResponseEntity.ok(boards);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
