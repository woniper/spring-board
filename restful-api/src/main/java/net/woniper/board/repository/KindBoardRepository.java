package net.woniper.board.repository;

import net.woniper.board.domain.KindBoard;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by woniper on 2015. 10. 30..
 */
public interface KindBoardRepository extends JpaRepository<KindBoard, Long> {

    KindBoard findByKindBoardName(String kindName);
}
