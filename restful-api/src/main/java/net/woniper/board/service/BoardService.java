package net.woniper.board.service;

import net.woniper.board.domain.Board;
import net.woniper.board.repository.BoardRepository;
import net.woniper.board.repository.UserRepository;
import net.woniper.board.support.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by woniper on 15. 4. 6..
 */
public interface BoardService {

    void setBoardRepository(BoardRepository boardRepository);

    void setUserRepository(UserRepository userRepository);

    Board createBoard(BoardDto boardDto, String username);

    Board getBoard(Long boardId);

    Page<Board> getBoard(Pageable pageable);

    Page<Board> getBoard(Pageable pageable, String username);

    Board updateBoard(Long boardId, BoardDto boardDto, String username);

    boolean deleteBoard(Long boardId, String username);

}
