package net.woniper.board.service.impl;

import net.woniper.board.domain.Board;
import net.woniper.board.repository.BoardRepository;
import net.woniper.board.service.SearchService;
import net.woniper.board.support.dto.SearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by woniper on 15. 9. 28..
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired private BoardRepository boardRepository;

    @Override
    public Page<Board> search(SearchDto searchDto, Pageable pageable) {
        // todo querydsl로 만들기
        return null;
    }
}
