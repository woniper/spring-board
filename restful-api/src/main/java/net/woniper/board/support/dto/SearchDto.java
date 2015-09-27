package net.woniper.board.support.dto;

import lombok.Data;
import net.woniper.board.support.type.SearchType;

/**
 * Created by woniper on 15. 9. 24..
 */
@Data
public class SearchDto {

    private SearchType type = SearchType.ALL;
    private String query = "";

    public String getLikeQuery() {
        return "%" + query + "%";
    }

}
