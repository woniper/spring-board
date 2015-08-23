package net.woniper.board.repository;

import net.woniper.board.domain.Comment;
import net.woniper.board.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by woniper on 15. 2. 7..
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByCommentIdAndUser(Long commentId, User user);
}
