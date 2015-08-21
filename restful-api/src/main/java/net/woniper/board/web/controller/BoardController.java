package net.woniper.board.web.controller;

import net.woniper.board.domain.Board;
import net.woniper.board.domain.Comment;
import net.woniper.board.domain.User;
import net.woniper.board.service.BoardService;
import net.woniper.board.service.CommentService;
import net.woniper.board.service.UserService;
import net.woniper.board.support.dto.BoardDto;
import net.woniper.board.support.dto.CommentDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * Created by woniper on 15. 1. 26..
 */
@RestController
@RequestMapping(value = "/boards", consumes = MediaType.APPLICATION_JSON_VALUE)
public class BoardController {

    @Autowired private BoardService boardService;
    @Autowired private UserService userService;
    @Autowired private ModelMapper modelMapper;
    @Autowired private CommentService commentService;

    /**
     * 게시글 생성
     * @param boardDto
     * @param result
     * @return
     */
//    @Secured("ROLE_USER")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createNewBoard(@RequestBody @Valid BoardDto boardDto, BindingResult result, Principal principal) {
        if(result.hasErrors()) {
            return new ResponseEntity<> (result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        Board board = boardService.createBoard(boardDto, principal.getName());
        BoardDto.Response responseBoard = getBoardResponse(board);

        return new ResponseEntity<> (responseBoard, HttpStatus.CREATED);
    }

    /**
     * 게시글 수정
     * @param board
     * @param result
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity updateBoard(@RequestBody @Valid Board board, BindingResult result, Principal principal) {

        if(result.hasErrors()) {
            return new ResponseEntity<> (result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        Board updateBoard = boardService.updateBoard(board, principal.getName());
        if(updateBoard != null) {
            BoardDto.Response responseBoard = getBoardResponse(updateBoard);
            return new ResponseEntity<> (responseBoard, HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<> (HttpStatus.NOT_ACCEPTABLE);

    }

    /**
     * 게시글 삭제
     * @param boardId
     * @return
     */
    @RequestMapping(value = "/{boardId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteBoard(@PathVariable("boardId") Long boardId, Principal principal) {
        if(boardService.deleteBoard(boardId, principal.getName()))
            return new ResponseEntity<> (HttpStatus.ACCEPTED);
        else
            return new ResponseEntity<> (HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * 게시글 조회
     * @param boardId
     * @return
     */
    @RequestMapping(value = "/{boardId}", method = RequestMethod.GET)
    public ResponseEntity getBoard(@PathVariable("boardId") Long boardId) {
        Board board = boardService.getBoard(boardId);
        BoardDto.Response responseBoard = getBoardResponse(board);
        User user = board.getUser();
        responseBoard.setUserId(user.getUserId());
        responseBoard.setUsername(user.getUsername());
        responseBoard.setNickName(user.getNickName());

        List<Comment> commentList = board.getComments();

        if(commentList != null && !commentList.isEmpty()) {
            List<CommentDto.Response> comments = modelMapper.map(commentList, new TypeToken<List<CommentDto.Response>>() {}.getType());

            int count = commentList.size();
            for (int i = 0; i < count; i++) {
                User commentUser = commentList.get(i).getUser();
                CommentDto.Response commentDto = comments.get(i);
                commentDto.setUserId(commentUser.getUserId());
                commentDto.setUsername(commentUser.getUsername());
                commentDto.setNickName(commentUser.getNickName());
                commentDto.setAdmin(commentUser.isAdmin());
            }

            responseBoard.setComments(comments);
        }
        return new ResponseEntity<> (responseBoard, HttpStatus.OK);
    }

    /**
     * 게시글 리스트 조회
     * @param pageable
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getBoards(Pageable pageable) {
        Page<Board> boards = boardService.getBoard(pageable);

        if(boards != null) {
            return ResponseEntity.ok(boards);
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
    @RequestMapping(value = "/{boardId}/comments", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity createNewComment(@RequestBody @Valid CommentDto commentDto,
                                           @PathVariable("boardId") Long boardId,
                                           BindingResult result,
                                           Principal principal) {
        if(result.hasErrors()) {
            return new ResponseEntity<> (result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        Comment comment = commentService.createComment(commentDto, boardId, principal.getName());
        CommentDto.Response responseComment = modelMapper.map(comment, CommentDto.Response.class);
        User user = comment.getUser();
        responseComment.setUsername(user.getUsername());
        responseComment.setNickName(user.getNickName());
        responseComment.setAdmin(user.isAdmin());

        return new ResponseEntity<> (responseComment, HttpStatus.CREATED);
    }

    private BoardDto.Response getBoardResponse(Board board) {
        BoardDto.Response responseBoard = modelMapper.map(board, BoardDto.Response.class);
        User user = board.getUser();
        responseBoard.setUserId(user.getUserId());
        responseBoard.setUsername(user.getUsername());
        responseBoard.setNickName(user.getNickName());
        responseBoard.setAdmin(user.isAdmin());
        return responseBoard;
    }

}
