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

/**
 * Created by woniper on 15. 1. 28..
 */
@Getter @Setter
public class UserDto extends ResourceSupport implements Serializable {

    @Size(min = 5) @NotEmpty private String username;
    @NotEmpty private String firstName;
    @NotEmpty private String lastName;
    @NotEmpty private String nickName;
    private AuthorityType authorityType;

    @ApiModel(value = "user Request")
    @Getter @Setter
    @ToString
    public static class Request extends UserDto {
        @Size(min = 5) @NotEmpty private String password;
    }

    @ApiModel(value = "user Response")
    @Getter @Setter
    @ToString
    public static class Response extends UserDto {
        private Long userId;
        private Date joinDate;
        private boolean active;
    }
}
