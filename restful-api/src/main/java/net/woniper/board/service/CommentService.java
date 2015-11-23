package net.woniper.board.service;

import net.woniper.board.domain.Comment;
import net.woniper.board.support.dto.CommentDto;

/**
 * Created by woniper on 15. 4. 6..
 */
public interface CommentService {

    Comment save(CommentDto commentDto, Long boardId);

    boolean delete(Long commentId, String username);

    Comment update(Long commentId, CommentDto commentDto, String username);
}
