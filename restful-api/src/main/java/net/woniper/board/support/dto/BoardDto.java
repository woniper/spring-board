package net.woniper.board.support.dto;

import com.wordnik.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.woniper.board.domain.type.AuthorityType;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by woniper on 15. 2. 1..
 */
@ApiModel(value = "board Request")
@Getter @Setter
public class BoardDto extends ResourceSupport implements Serializable {

    @NotEmpty @Size(min = 2)
    private String title;

    @NotEmpty @Size(min = 2)
    private String content;

    public BoardDto() {}

    public BoardDto(String title, String content) {
        setTitle(title);
        setContent(content);
    }

    @ApiModel(value = "board Response")
    @Getter @Setter
    public static class Response extends ListResponse {
        private List<CommentDto.Response> comments;
    }

    @ApiModel(value = "board list Response")
    @Getter @Setter
    @ToString
    public static class ListResponse extends BoardDto {
        private Long boardId;
        private int readCount;
        private Date createDate;

        private Long userId;
        private String username;
        private String nickName;
        private AuthorityType authorityType;
    }


}
