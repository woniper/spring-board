package net.woniper.board.service;

import net.woniper.board.domain.Comment;
import net.woniper.board.support.dto.CommentDto;

/**
 * Created by woniper on 15. 4. 6..
 */
public interface CommentService {

    Comment createComment(CommentDto commentDto, Long boardId, String username);

    boolean deleteComment(Long commentId, String username);
}
