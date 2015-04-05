package net.woniper.board.support.dto;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/**
 * Created by woniper on 15. 1. 27..
 */
@Data
public class BoardListDto extends ResourceSupport {

    private List<BoardDto.Response> boards;
    private int currentPage;
    private int beginPage;
    private int endPage;

}
