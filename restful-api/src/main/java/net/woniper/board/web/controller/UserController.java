package net.woniper.board.web.controller;

import com.wordnik.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import net.woniper.board.domain.Board;
import net.woniper.board.domain.User;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.service.BoardService;
import net.woniper.board.service.UserService;
import net.woniper.board.support.dto.BoardDto;
import net.woniper.board.support.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * Created by woniper on 15. 1. 28..
 */
@Api(value = "/users", description = "user account, update, delete")
@RestController
@RequestMapping(value = "/users")
@Slf4j
public class UserController {

    @Autowired private UserService userService;
    @Autowired private BoardService boardService;
    @Autowired private ModelMapper modelMapper;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(AuthorityType.class, new AuthorityType.AuthorityTypeProperty());
    }

    /**
     * 회원가입
     * @param userDto
     * @param result
     * @return
     */
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
        log.info("user account : {}", newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(newUser, UserDto.Response.class));
    }

    /**
     * 회원 정보 수정
     * @param userDto
     * @param result
     * @param principal
     * @param request
     * @return
     */
    @ApiOperation(value = "update user", response = UserDto.Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "success update user", response = UserDto.Response.class),
            @ApiResponse(code = 406, message = "fail update user"),
            @ApiResponse(code = 400, message = "Bad Request", response = ObjectError.class)
    })
    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@ApiParam(required = true) @RequestBody @Valid UserDto.Request userDto,
                                        BindingResult result, Principal principal, HttpServletRequest request) {
        if(result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        User updateUser = userService.updateUser(userDto, principal.getName(), request.getMethod());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(modelMapper.map(updateUser, UserDto.Response.class));
    }

    /**
     * 회원 탈퇴 (비활성화)
     * @param principal
     * @return
     */
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

    /**
     * 회원 조회
     * @param userId
     * @param principal
     * @return
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable("userId") Long userId, Principal principal) {
        User user = userService.getUser(userId, principal.getName());
        return ResponseEntity.ok(modelMapper.map(user, UserDto.Response.class));
    }

    /**
     * 회원 리스트 조회
     * @param pageable
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getUsers(Pageable pageable) {
        Page<User> users = userService.getUser(pageable);

        if(users != null) {
            List<User> userList = users.getContent();
            List<UserDto.Response> userListResponse = modelMapper.map(userList,
                    new TypeToken<List<UserDto.Response>>(){}.getType());

            if(userListResponse != null && !userListResponse.isEmpty()) {
                return ResponseEntity.ok(new PageImpl<>(userListResponse, pageable, users.getTotalElements()));
            }
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 회원이 생성한 board 리스트 조회
     * @param pageable
     * @param principal
     * @return
     */
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

            List<Board> boardList = boards.getContent();
            List<BoardDto.ListResponse> boardListResponses = modelMapper.map(boardList,
                    new TypeToken<List<BoardDto.ListResponse>>() {
                    }.getType());

            if(boardListResponses != null && !boardListResponses.isEmpty()) {
                int size = boardListResponses.size();

                for (int i = 0; i < size; i++) {
                    User user = boardList.get(i).getUser();
                    BoardDto.ListResponse boardDto = boardListResponses.get(i);

                    boardDto.setUserId(user.getUserId());
                    boardDto.setUsername(user.getUsername());
                    boardDto.setNickName(user.getNickName());
                    boardDto.setAuthorityType(user.getAuthorityType());
                }
                Page<BoardDto.ListResponse> boardPages = new PageImpl<>(boardListResponses, pageable, boards.getTotalElements());
                return ResponseEntity.ok(boardPages);
            }
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
