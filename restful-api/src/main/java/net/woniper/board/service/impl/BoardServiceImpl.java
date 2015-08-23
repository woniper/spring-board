package net.woniper.board.service.impl;

import net.woniper.board.domain.Board;
import net.woniper.board.domain.User;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.repository.BoardRepository;
import net.woniper.board.repository.UserRepository;
import net.woniper.board.service.BoardService;
import net.woniper.board.support.dto.BoardDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * Created by woniper on 15. 1. 26..
 */
@Service
@Transactional
public class BoardServiceImpl implements BoardService {

    private BoardRepository boardRepository;
    private UserRepository userRepository;
    @Autowired private ModelMapper modelMapper;

    @Autowired
    @Override
    public void setBoardRepository(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }
    @Autowired
    @Override
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Board createBoard(BoardDto boardDto, String username) {
        Board board = new Board();
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        User user = userRepository.findByUsername(username);
        user.setBoards(Arrays.asList(board));
        board.setUser(user);
        return boardRepository.save(board);
    }

    @Override
    public Board getBoard(Long boardId) {
        Board board = boardRepository.findOne(boardId);
        if(board != null)
            board.setReadCount(board.getReadCount() + 1);

        boardRepository.flush();
        return board;
    }

    @Override
    public Page<Board> getBoard(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Override
    public Page<Board> getBoard(Pageable pageable, String username) {
        User user = userRepository.findByUsername(username);
        if(user != null) {
            return boardRepository.findByUser(user, pageable);
        }

        return null;
    }

    @Override
    public Board updateBoard(Board board, String username) {
        User user = userRepository.findByUsername(username);
        Board oldBoard = null;
        if(AuthorityType.ADMIN.equals(user.getAuthorityType())) {
            oldBoard = boardRepository.findOne(board.getBoardId());
        } else{
            oldBoard = boardRepository.findByBoardIdAndUser(board.getBoardId(), user);
        }

        if(oldBoard != null) {
            oldBoard.setTitle(board.getTitle());
            oldBoard.setContent(board.getContent());
            boardRepository.flush();
        }
        return oldBoard;
    }

    @Override
    public boolean deleteBoard(Long boardId, String username) {
        User user = userRepository.findByUsername(username);
        Board board = null;

        if(AuthorityType.ADMIN.equals(user.getAuthorityType())) {
            board = boardRepository.findOne(boardId);
        } else {
            board = boardRepository.findByBoardIdAndUser(boardId, user);
        }

        if(board != null) {
            boardRepository.delete(board);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 게시글 삭제, 수정 가능한 계정인지 확인
     * admin 계정 OR 게시글을 등록한 계정만 가능
     * @param boardId
     * @param username
     * @return
     */
//    public boolean isBoardAccessRole(Long boardId, String username) {
//        Board board = boardRepository.findOne(boardId);
//        User user = userRepository.findByUsername(username);
//
//        if(board != null && user != null) {
//            if(user.isAdmin()) return true;
//            if(board.getUser().getUserId().equals(user.getUserId())) return true;
//        }
//
//        return false;
//    }
}
