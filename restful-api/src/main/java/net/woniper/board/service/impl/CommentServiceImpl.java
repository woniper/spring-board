package net.woniper.board.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.woniper.board.domain.Board;
import net.woniper.board.domain.Comment;
import net.woniper.board.domain.User;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.errors.support.CommentNotFoundException;
import net.woniper.board.repository.CommentRepository;
import net.woniper.board.service.BoardService;
import net.woniper.board.service.CommentService;
import net.woniper.board.service.UserService;
import net.woniper.board.support.dto.CommentDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by woniper on 15. 2. 7..
 */
@Service
@Transactional
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired private CommentRepository commentRepository;
    @Autowired private BoardService boardService;
    @Autowired private UserService userService;
    @Autowired private ModelMapper modelMapper;

    @Override
    public Comment save(CommentDto commentDto, Long boardId) {
        Board board = boardService.find(boardId);
        Comment comment = modelMapper.map(commentDto, Comment.class);
        comment.setBoard(board);
        return commentRepository.save(comment);
    }

    @Override
    public boolean delete(Long commentId, String username) {
        User user = userService.find(username);
        Comment comment = getComment(commentId);

        if(isAccessCommentUser(comment, user)) {
            commentRepository.delete(comment);
            return true;
        } else {
            throw new AccessDeniedException("accessDenied " + username);
        }
    }

    @Override
    public Comment update(Long commentId, CommentDto commentDto, String username) {
        User user = userService.find(username);
        Comment comment = getComment(commentId);
        if(isAccessCommentUser(comment, user)) {
            comment.patch(commentDto);
        } else {
            throw new AccessDeniedException("accessDenied " + username);
        }
        return comment;
    }

    private Comment getComment(Long commentId) {
        Comment comment = commentRepository.getOne(commentId);
        if(comment == null)
            throw new CommentNotFoundException(commentId);

        return comment;
    }

    private boolean isAccessCommentUser(Comment comment, User user) {
        return AuthorityType.ADMIN == user.getAuthorityType() ||
               user.getUserId().equals(comment.getBoard().getUser().getUserId());
    }
}
