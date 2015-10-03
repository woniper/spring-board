package net.woniper.board.support.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.woniper.board.support.type.SearchType;

/**
 * Created by woniper on 15. 9. 24..
 */
@AllArgsConstructor
@Getter @Setter
public class SearchDto {

    private SearchType type = SearchType.ALL;
    private String query = "";

    public String getLikeQuery() {
        return "%" + query + "%";
    }

}
