package net.woniper.board.support.dto;

import lombok.Data;
import net.woniper.board.domain.type.AuthorityType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by woniper on 15. 2. 1..
 */
@Data
public class BoardDto implements Serializable {

    @NotNull @Size(min = 2)
    private String title;

    @NotNull @Size(min = 2)
    private String content;

    public BoardDto() {}

    public BoardDto(String title, String content) {
        setTitle(title);
        setContent(content);
    }

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
