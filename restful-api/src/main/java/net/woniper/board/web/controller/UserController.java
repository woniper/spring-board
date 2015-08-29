package net.woniper.board.web.controller;

import com.wordnik.swagger.annotations.*;
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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Created by woniper on 15. 1. 28..
 */
@Api(value = "/users", description = "user account, update, delete")
@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired private UserService userService;
    @Autowired private BoardService boardService;
    @Autowired private ModelMapper modelMapper;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(AuthorityType.class, new AuthorityType.AuthorityTypeProperty());
    }

    @ApiOperation(value = "account user", response = UserDto.Response.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "success account user", response = UserDto.Response.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ObjectError.class)
    })
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewUser(@ApiParam(required = true) @RequestBody @Valid UserDto.Request userDto,
                                           BindingResult result) {

        if(result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        User newUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(newUser, UserDto.Response.class));
    }

    @ApiOperation(value = "update user", response = UserDto.Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "success update user", response = UserDto.Response.class),
            @ApiResponse(code = 406, message = "fail update user"),
            @ApiResponse(code = 400, message = "Bad Request", response = ObjectError.class)
    })
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@ApiParam(required = true) @RequestBody @Valid UserDto.Request userDto,
                                        BindingResult result,
                                        Principal principal) {
        if(result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        User updateUser = userService.updateUser(userDto, principal.getName());
        if(updateUser != null)
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(modelMapper.map(updateUser, UserDto.Response.class));

        return new ResponseEntity<> (HttpStatus.NOT_ACCEPTABLE);
    }

    @ApiOperation(value = "delete user")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "success delete user"),
            @ApiResponse(code = 400, message = "fail delete user")
    })
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(Principal principal) {
        if(userService.deleteUser(principal.getName()))
            return new ResponseEntity<> (HttpStatus.ACCEPTED);

        return new ResponseEntity<> (HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "get user boards")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success find user board"),
            @ApiResponse(code = 204, message = "No Content")
    })
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "page", value = "page", dataType = "Long", paramType = "query", required = false),
            @ApiImplicitParam(name = "size", value = "size", dataType = "Long", paramType = "query", required = false),
            @ApiImplicitParam(name = "sort", value = "ex) sort=name,ase", dataType = "String", paramType = "query", required = false)
    })
    @RequestMapping(value = "/boards", method = RequestMethod.GET)
    public ResponseEntity<?> getUserBoardList(Pageable pageable,
                                              Principal principal) {
        Page<Board> boards = boardService.getBoard(pageable, principal.getName());

        if(boards != null) {
            return ResponseEntity.ok(boards);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
