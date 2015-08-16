package demo.Board;

import demo.generic.GenericService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Created by woniper on 15. 4. 27..
 */
@Service
public class BoardService extends GenericService<Board, Integer, JpaRepository<Board, Integer>> {
}
