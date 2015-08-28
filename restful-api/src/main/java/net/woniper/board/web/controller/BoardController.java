package net.woniper.board.web.controller;

import com.wordnik.swagger.annotations.*;
import net.woniper.board.domain.Board;
import net.woniper.board.domain.Comment;
import net.woniper.board.domain.User;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.service.BoardService;
import net.woniper.board.service.CommentService;
import net.woniper.board.service.UserService;
import net.woniper.board.support.dto.BoardDto;
import net.woniper.board.support.dto.CommentDto;
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

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * Created by woniper on 15. 1. 26..
 */
@RestController
@RequestMapping(value = "/boards", produces = MediaType.APPLICATION_JSON_VALUE)
public class BoardController {

    @Autowired private BoardService boardService;
    @Autowired private UserService userService;
    @Autowired private ModelMapper modelMapper;
    @Autowired private CommentService commentService;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(AuthorityType.class, new AuthorityType.AuthorityTypeProperty());
    }

    /**
     * 게시글 생성
     * @param boardDto
     * @param result
     * @return
     */
    @ApiOperation(value = "insert board")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "success insert board", response = BoardDto.Response.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ObjectError.class)
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createNewBoard(@ApiParam(required = true) @RequestBody @Valid BoardDto boardDto,
                                            BindingResult result,
                                            Principal principal) {
        if(result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        Board board = boardService.createBoard(boardDto, principal.getName());
        BoardDto.Response responseBoard = getBoardResponse(board);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBoard);
    }

    /**
     * 게시글 수정
     * @param board
     * @param result
     * @return
     */
    @ApiOperation(value = "update board")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "success insert board", response = BoardDto.Response.class),
            @ApiResponse(code = 406, message = "fail insert board"),
            @ApiResponse(code = 400, message = "Bad Request", response = ObjectError.class)
    })
    @RequestMapping(value = "/{boardId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateBoard(@ApiParam(required = true) @PathVariable("boardId") Long boardId,
                                         @ApiParam(required = true) @RequestBody @Valid BoardDto board,
                                         BindingResult result,
                                         Principal principal) {

        if(result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        Board updateBoard = boardService.updateBoard(boardId, board, principal.getName());
        if(updateBoard != null) {
            BoardDto.Response responseBoard = getBoardResponse(updateBoard);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBoard);
        }

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();

    }

    /**
     * 게시글 삭제
     * @param boardId
     * @return
     */
    @ApiOperation(value = "delete board")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "success delete board"),
            @ApiResponse(code = 406, message = "fail delete board")
    })
    @RequestMapping(value = "/{boardId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteBoard(@ApiParam(required = true) @PathVariable("boardId") Long boardId,
                                         Principal principal) {
        if(boardService.deleteBoard(boardId, principal.getName()))
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        else
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    /**
     * 게시글 조회
     * @param boardId
     * @return
     */
    @ApiOperation(value = "get board")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success get board", response = BoardDto.Response.class),
            @ApiResponse(code = 204, message = "No Content")
    })
    @RequestMapping(value = "/{boardId}", method = RequestMethod.GET)
    public ResponseEntity<?> getBoard(@ApiParam(required = true) @PathVariable("boardId") Long boardId) {
        Board board = boardService.getBoard(boardId);

        if(board != null) {
            BoardDto.Response responseBoard = getBoardResponse(board);
            List<Comment> commentList = board.getComments();

            if(commentList != null && !commentList.isEmpty()) {
                List<CommentDto.Response> comments = modelMapper.map(commentList,
                        new TypeToken<List<CommentDto.Response>>() {}.getType());

                int size = commentList.size();
                for (int i = 0; i < size; i++) {
                    User commentUser = commentList.get(i).getUser();
                    CommentDto.Response commentDto = comments.get(i);

                    commentDto.setUserId(commentUser.getUserId());
                    commentDto.setUsername(commentUser.getUsername());
                    commentDto.setNickName(commentUser.getNickName());
                    commentDto.setAuthorityType(commentUser.getAuthorityType());
                }

                responseBoard.setComments(comments);
            }
            return ResponseEntity.ok(responseBoard);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 게시글 리스트 조회
     * @param pageable
     * @return
     */
    @ApiOperation(value = "get board list")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success get board", response = BoardDto.Response.class),
            @ApiResponse(code = 204, message = "No Content")
    })
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "page", value = "page", dataType = "Long", paramType = "query", required = false),
            @ApiImplicitParam(name = "size", value = "size", dataType = "Long", paramType = "query", required = false),
            @ApiImplicitParam(name = "sort", value = "ex) sort=name,ase", dataType = "String", paramType = "query", required = false)
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getBoards(Pageable pageable) {
        Page<Board> boards = boardService.getBoard(pageable);

        if(boards != null) {
            List<Board> boardList = boards.getContent();
            List<BoardDto.ListResponse> boardContents = modelMapper.map(boardList,
                    new TypeToken<List<BoardDto.ListResponse>>() {}.getType());

            if(boardContents != null) {
                int size = boardContents.size();
                for (int i = 0; i < size; i++) {
                    User user = boardList.get(i).getUser();
                    BoardDto.ListResponse boardDto = boardContents.get(i);

                    boardDto.setUserId(user.getUserId());
                    boardDto.setUsername(user.getUsername());
                    boardDto.setNickName(user.getNickName());
                    boardDto.setAuthorityType(user.getAuthorityType());
                }

                Page<BoardDto.ListResponse> boardPages = new PageImpl<BoardDto.ListResponse>(boardContents, pageable, boards.getTotalElements());
                return ResponseEntity.ok(boardPages);
            }
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 게시판 댓글 입력
     * @param commentDto
     * @param boardId
     * @param result
     * @param principal
     * @return
     */
    @ApiOperation(value = "insert comment")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "success insert comment", response = CommentDto.Response.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ObjectError.class),
            @ApiResponse(code = 400, message = "Bad Request(No Content)")
    })
    @RequestMapping(value = "/{boardId}/comments", method = RequestMethod.POST)
    public ResponseEntity<?> createNewComment(@ApiParam(required = true) @RequestBody @Valid CommentDto commentDto,
                                              @ApiParam(required = true) @PathVariable("boardId") Long boardId,
                                              BindingResult result,
                                              Principal principal) {
        if(result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        Comment comment = commentService.createComment(commentDto, boardId, principal.getName());
        if(comment != null) {
            CommentDto.Response responseComment = modelMapper.map(comment, CommentDto.Response.class);
            User user = comment.getUser();
            responseComment.setUsername(user.getUsername());
            responseComment.setNickName(user.getNickName());
            responseComment.setAuthorityType(user.getAuthorityType());

            return ResponseEntity.status(HttpStatus.CREATED).body(responseComment);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ApiOperation(value = "delete comment")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "success delete comment"),
            @ApiResponse(code = 406, message = "fail delete comment")
    })
    @RequestMapping(value = "/comments/{commentId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteComment(@ApiParam(required = true) @PathVariable("commentId") Long commentId,
                                           Principal principal) {
        if(commentService.deleteComment(commentId, principal.getName())) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @ApiOperation(value = "update comment")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "success update comment", response = CommentDto.Response.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ObjectError.class),
            @ApiResponse(code = 406, message = "fail update comment")
    })
    @RequestMapping(value = "/comments/{commentId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateComment(@ApiParam(required = true) @PathVariable("commentId") Long commentId,
                                           @ApiParam(required = true) @RequestBody @Valid CommentDto commentDto,
                                           BindingResult result,
                                           Principal principal) {

        if(result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        Comment comment = commentService.updateComment(commentId, commentDto, principal.getName());
        if(comment != null) {
            CommentDto.Response responseComment = modelMapper.map(comment, CommentDto.Response.class);
            User user = comment.getUser();
            responseComment.setUsername(user.getUsername());
            responseComment.setNickName(user.getNickName());
            responseComment.setAuthorityType(user.getAuthorityType());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseComment);
        }

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    private BoardDto.Response getBoardResponse(Board board) {
        BoardDto.Response responseBoard = modelMapper.map(board, BoardDto.Response.class);
        User user = board.getUser();
        responseBoard.setUserId(user.getUserId());
        responseBoard.setUsername(user.getUsername());
        responseBoard.setNickName(user.getNickName());
        responseBoard.setAuthorityType(user.getAuthorityType());
        return responseBoard;
    }

}
