package net.woniper.board.service;

import net.woniper.board.domain.Board;
import net.woniper.board.repository.BoardRepository;
import net.woniper.board.repository.UserRepository;
import net.woniper.board.support.dto.BoardDto;
import net.woniper.board.support.dto.BoardListDto;

/**
 * Created by woniper on 15. 4. 6..
 */
public interface BoardService {

    void setBoardRepository(BoardRepository boardRepository);

    void setUserRepository(UserRepository userRepository);

    Board createBoard(BoardDto boardDto, String username);

    Board getBoard(Long boardId);

    Board updateBoard(Board board, String username);

    boolean deleteBoard(Long boardId, String username);

    BoardListDto getBoardList(int page, int limit, String orderBy, String orderDir, Class clz);

    BoardListDto getUserBoardList(Long userId, int page, int limit, String orderBy, String orderDir, Class clz);
}
