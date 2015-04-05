package net.woniper.board.support.paging;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Created by woniper on 15. 4. 6..
 */
public class BoardPageableHelper {

    private static final String ORDER_DIRECTION_DESC = "DESC";
    private static final String ORDER_DIRECTION_ASC = "ASC";

    public static Pageable createBoardPageable(int page, int limit, String orderDir, String orderBy) {
        if(page <= 0)
            page = 1;

        if(limit <= 0)
            limit = 20;

        Sort.Direction direction = Sort.Direction.DESC;
        if(ORDER_DIRECTION_ASC.equalsIgnoreCase(orderDir)) {
            direction = Sort.Direction.ASC;
        }
        return new PageRequest(page - 1, limit, direction, orderBy);
    }
}
