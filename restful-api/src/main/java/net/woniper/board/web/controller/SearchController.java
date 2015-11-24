package net.woniper.board.web.controller;

import net.woniper.board.domain.Board;
import net.woniper.board.domain.User;
import net.woniper.board.service.SearchService;
import net.woniper.board.support.dto.BoardDto;
import net.woniper.board.support.dto.SearchDto;
import net.woniper.board.support.type.SearchType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by woniper on 15. 9. 24..
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired private SearchService searchService;
    @Autowired private ModelMapper modelMapper;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(SearchType.class, new SearchType.SearchTypeProperty());
    }

    /**
     * 조회
     * @param searchDto
     * @param pageable
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity search(SearchDto searchDto, Pageable pageable) {
        Page<Board> boards = searchService.search(searchDto, pageable);
        List<Board> boardContents = boards.getContent();
        List<BoardDto.ListResponse> boardListResponses = boardContents.parallelStream()
                .map(board -> modelMapper.map(board, BoardDto.ListResponse.class))
                .collect(Collectors.toList());

        int size = boardContents.size();
        for (int i = 0; i < size; i++) {
            User user = boardContents.get(i).getUser();
            BoardDto.ListResponse boardDto = boardListResponses.get(i);
            boardDto.setUserId(user.getUserId());
            boardDto.setUsername(user.getUsername());
            boardDto.setNickName(user.getNickName());
            boardDto.setAuthorityType(user.getAuthorityType());

            boardDto.setKindBoardName(boardContents.get(i).getKindBoard().getKindBoardName());
        }

        Page<BoardDto.ListResponse> boardPages = new PageImpl<>(boardListResponses, pageable, boards.getTotalElements());
        return ResponseEntity.ok(boardPages);
    }

}
