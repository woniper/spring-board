package net.woniper.board.support.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.woniper.board.support.type.SearchType;
import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;

/**
 * Created by woniper on 15. 9. 24..
 */
@AllArgsConstructor
@Getter @Setter
public class SearchDto extends ResourceSupport  implements Serializable {

    private SearchType type = SearchType.ALL;
    private String query = "";

    public String getLikeQuery() {
        return "%" + query + "%";
    }

}
