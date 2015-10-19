package net.woniper.board.test.service;

import net.woniper.board.builder.EntityBuilder;
import net.woniper.board.domain.Board;
import net.woniper.board.domain.User;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.service.BoardService;
import net.woniper.board.service.SearchService;
import net.woniper.board.service.UserService;
import net.woniper.board.support.dto.BoardDto;
import net.woniper.board.support.dto.SearchDto;
import net.woniper.board.support.type.SearchType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by woniper on 15. 2. 4..
 */
public class SearchServiceTest extends BaseServiceTest {

    @Autowired private SearchService searchService;
    @Autowired private UserService userService;
    @Autowired private BoardService boardService;

    private Pageable pageable;

    @Before
    public void setUp() throws Exception {
        fixture();
        this.pageable = new PageRequest(0, 10);
    }

    @Test
    public void test_search_all_title() throws Exception {
        // given
        SearchDto searchDto = new SearchDto(SearchType.ALL, "tt");

        // when
        Page<Board> boards = searchService.search(searchDto, pageable);
        List<Board> boardList = boards.getContent();

        // then
        assertEquals(3, boardList.size());
    }

    @Test
    public void test_search_all_content() throws Exception {
        // given
        SearchDto searchDto = new SearchDto(SearchType.ALL, "cc");

        // when
        List<Board> boards = searchService.search(searchDto, pageable).getContent();

        // then
        assertEquals(3, boards.size());
    }

    @Test
    public void test_search_title() throws Exception {
        // given
        SearchDto searchDto = new SearchDto(SearchType.TITLE, "tt1");

        // when
        List<Board> boards = searchService.search(searchDto, pageable).getContent();

        // then
        assertEquals(1, boards.size());
    }

    @Test
    public void test_search_content() throws Exception {
        // given
        SearchDto searchDto = new SearchDto(SearchType.CONTENT, "cc1");

        // when
        List<Board> boards = searchService.search(searchDto, pageable).getContent();

        // then
        assertEquals(1, boards.size());
    }

    private void fixture() {
        User user = userService.createUser(EntityBuilder.createUser(AuthorityType.USER));
        boardService.createBoard(new BoardDto.Request("tt1", "cc1"), user.getUsername());
        boardService.createBoard(new BoardDto.Request("tt2", "cc2"), user.getUsername());
        boardService.createBoard(new BoardDto.Request("tt3", "cc3"), user.getUsername());
    }
}