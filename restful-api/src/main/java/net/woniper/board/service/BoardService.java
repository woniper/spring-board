package net.woniper.board.service;

import net.woniper.board.domain.Board;
import net.woniper.board.support.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by woniper on 15. 4. 6..
 */
public interface BoardService {

    Board save(BoardDto.Request boardDto, String username);

    Board find(Long boardId);

    Page<Board> find(Pageable pageable);

    Page<Board> find(Pageable pageable, String username);

    Board update(Long boardId, BoardDto.Request boardDto, String username, String method);

    boolean delete(Long boardId, String username);

}
