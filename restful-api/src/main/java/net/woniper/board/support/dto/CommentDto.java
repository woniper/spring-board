package net.woniper.board.support.dto;

import lombok.Data;
import net.woniper.board.domain.type.AuthorityType;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by woniper on 15. 2. 4..
 */
@Data
public class CommentDto implements Serializable {

    private String content;

    @Data
    public static class Response extends CommentDto {
        private Long commentId;
        private int depth;
        private Date createDate = new Date();

        private Long userId;
        private String username;
        private String nickName;
        private AuthorityType authorityType;
    }

}
