package demo.Board;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by woniper on 15. 4. 27..
 */
public interface BoardRepository extends JpaRepository<Board, Integer>{
}
