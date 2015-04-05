package net.woniper.board.repository;

import net.woniper.board.domain.Board;
import net.woniper.board.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by woniper on 15. 1. 26..
 */
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findByUser(User user, Pageable pageable);

    Board findByBoardIdAndUser(Long boardId, User byUsername);
}
