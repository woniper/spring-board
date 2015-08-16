package demo.Board;

import demo.generic.GenericController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by woniper on 15. 4. 27..
 */
@RestController
@RequestMapping(value = "/board")
public class BoardController extends GenericController<Board, Integer, BoardService> {

}