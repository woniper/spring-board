package net.woniper.board.service;

import net.woniper.board.domain.Board;
import net.woniper.board.support.dto.SearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by woniper on 15. 9. 24..
 */
public interface SearchService {

    Page<Board> search(SearchDto searchDto, Pageable pageable);

}
