package net.woniper.board.service.impl;

import net.woniper.board.domain.Board;
import net.woniper.board.domain.Comment;
import net.woniper.board.domain.User;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.repository.BoardRepository;
import net.woniper.board.repository.CommentRepository;
import net.woniper.board.repository.UserRepository;
import net.woniper.board.service.CommentService;
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
public class CommentServiceImpl implements CommentService {

    @Autowired private CommentRepository commentRepository;
    @Autowired private BoardRepository boardRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ModelMapper modelMapper;

    @Override
    public Comment createComment(CommentDto commentDto, Long boardId) {
        Board board = boardRepository.findOne(boardId);
        if(board != null) {
            Comment comment = modelMapper.map(commentDto, Comment.class);
            comment.setBoard(board);
            return commentRepository.save(comment);
        }
        return null;
    }

    @Override
    public boolean deleteComment(Long commentId, String username) {
        User user = userRepository.findByUsername(username);
        if(user != null) {
            Comment comment = commentRepository.findOne(commentId);
            if(comment != null) {
                if(isAccessCommentUser(comment, user)) {
                    commentRepository.delete(comment);
                    return true;
                } else {
                    throw new AccessDeniedException("accessDenied " + username);

                }
            }
        }

        return false;
    }

    @Override
    public Comment updateComment(Long commentId, CommentDto commentDto, String username) {
        User user = userRepository.findByUsername(username);
        Comment comment = commentRepository.findOne(commentId);
        if(comment != null) {
            if(isAccessCommentUser(comment, user)) {
                comment.patch(commentDto);
            } else {
                throw new AccessDeniedException("accessDenied " + username);
            }
        }
        return comment;
    }

    private boolean isAccessCommentUser(Comment comment, User user) {
        return AuthorityType.ADMIN == user.getAuthorityType() ||
               user.getUserId().equals(comment.getBoard().getUser().getUserId());
    }
}
