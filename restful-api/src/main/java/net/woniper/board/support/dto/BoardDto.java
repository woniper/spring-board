package net.woniper.board.support.dto;

import lombok.Data;
import net.woniper.board.domain.type.AuthorityType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by woniper on 15. 2. 1..
 */
@Data
public class BoardDto implements Serializable {

    private String title;
    private String content;

    @Data
    public static class Response extends BoardDto {
        private Long boardId;
        private int readCount;
        private Date createDate = new Date();

        private Long userId;
        private String username;
        private String nickName;
        private AuthorityType authorityType;

        private List<CommentDto.Response> comments;
    }
}
