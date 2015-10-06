package net.woniper.board.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.woniper.board.domain.Board;
import net.woniper.board.domain.User;
import net.woniper.board.errors.support.BoardNotFoundException;
import net.woniper.board.repository.BoardRepository;
import net.woniper.board.repository.UserRepository;
import net.woniper.board.service.SearchService;
import net.woniper.board.support.dto.SearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by woniper on 15. 9. 28..
 */
@Service
@Transactional
@Slf4j
public class SearchServiceImpl implements SearchService {

    @Autowired private UserRepository userRepository;
    @Autowired private BoardRepository boardRepository;

    @Override
    public Page<Board> search(SearchDto searchDto, Pageable pageable) {
        Page<Board> boards = null;
        switch (searchDto.getType()) {
            case WRITER:
                User user = userRepository.findByNickName(searchDto.getQuery());
                boards = boardRepository.findByUser(user, pageable);
                break;
            case TITLE:
                boards = boardRepository.findByTitleLike(searchDto.getLikeQuery(), pageable);
                break;
            case CONTENT:
                boards = boardRepository.findByContentLike(searchDto.getLikeQuery(), pageable);
                break;
            case ALL:
                boards = boardRepository.findByTitleLikeOrContentLike(searchDto.getLikeQuery(), searchDto.getLikeQuery(), pageable);
                break;
        }

        if(boards == null)
            throw new BoardNotFoundException(0L);

        return boards;
    }
}
