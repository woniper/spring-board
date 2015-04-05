package net.woniper.board.service.impl;

import net.woniper.board.domain.Board;
import net.woniper.board.domain.User;
import net.woniper.board.repository.BoardRepository;
import net.woniper.board.repository.UserRepository;
import net.woniper.board.service.BoardService;
import net.woniper.board.support.dto.BoardDto;
import net.woniper.board.support.dto.BoardListDto;
import net.woniper.board.support.paging.BoardPageableHelper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by woniper on 15. 1. 26..
 */
@Service
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
    public Board updateBoard(Board board, String username) {
        User user = userRepository.findByUsername(username);
        Board oldBoard = null;
        if(user.isAdmin()) {
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

        if(user.isAdmin()) {
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

    @Override
    public BoardListDto getBoardList(int page, int limit, String orderBy, String orderDir, Class clz) {
        Pageable pageRequest = BoardPageableHelper.createBoardPageable(page, limit, orderDir, orderBy);
        Page<Board> boards = boardRepository.findAll(pageRequest);
        if(boards != null) {
            BoardListDto boardListDto = new BoardListDto();
            int currentPage = boards.getNumber() + 1;
            int beginPage = Math.max(1, currentPage - 5);
            int endPage = Math.min(beginPage + limit, boards.getTotalPages());

            List<Board> boardList = boards.getContent();
            List<BoardDto.Response> boardDtos = modelMapper.map(boardList, new TypeToken<List<BoardDto.Response>>() {}.getType());
            int count = boardList.size();
            for (int i = 0; i < count; i++) {
                User user = boardList.get(i).getUser();
                BoardDto.Response responseBoard = boardDtos.get(i);
                responseBoard.setUserId(user.getUserId());
                responseBoard.setUsername(user.getUsername());
                responseBoard.setNickName(user.getNickName());
                responseBoard.setAdmin(user.isAdmin());
            }

            boardListDto.setBoards(boardDtos);
            boardListDto.setCurrentPage(currentPage);
            boardListDto.setBeginPage(beginPage);
            boardListDto.setEndPage(endPage);

            List<Link> linkList = new ArrayList<>();
            if(!boards.isFirst())
                linkList.add(linkTo(clz).slash("boards").slash(currentPage - 1).withRel("prePage"));

            if(!boards.isLast())
                linkList.add(linkTo(clz).slash("boards").slash(currentPage + 1).withRel("nextPage"));

            boardListDto.add(linkList);
            return boardListDto;
        }

        return null;
    }

    @Override
    public BoardListDto getUserBoardList(Long userId, int page, int limit, String orderBy, String orderDir, Class clz) {
        Pageable pageRequest = BoardPageableHelper.createBoardPageable(page, limit, orderDir, orderBy);
        Page<Board> boards = boardRepository.findByUser(userRepository.findOne(userId), pageRequest);

        if(boards != null) {
            BoardListDto boardListDto = new BoardListDto();

            int currentPage = boards.getNumber() + 1;
            int beginPage = Math.max(1, currentPage - 5);
            int endPage = Math.min(beginPage + limit, boards.getTotalPages());

            List<Board> boardList = boards.getContent();
            List<BoardDto.Response> boardDtos = modelMapper.map(boardList, new TypeToken<List<BoardDto.Response>>() {}.getType());
            int count = boardList.size();
            for (int i = 0; i < count; i++) {
                User user = boardList.get(i).getUser();
                BoardDto.Response responseBoard = boardDtos.get(i);
                responseBoard.setUserId(user.getUserId());
                responseBoard.setUsername(user.getUsername());
                responseBoard.setNickName(user.getNickName());
                responseBoard.setAdmin(user.isAdmin());
            }

            boardListDto.setBoards(boardDtos);
            boardListDto.setCurrentPage(currentPage);
            boardListDto.setBeginPage(beginPage);
            boardListDto.setEndPage(endPage);

            List<Link> linkList = new ArrayList<>();

            if(!boards.isFirst())
                linkList.add(linkTo(clz).slash("users").slash("boards").slash(currentPage - 1).withRel("prePage"));

            if(!boards.isLast())
                linkList.add(linkTo(clz).slash("users").slash("boards").slash(currentPage + 1).withRel("nextPage"));

            boardListDto.add(linkList);
            return boardListDto;
        }

        return null;
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
