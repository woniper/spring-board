package net.woniper.board.support.dto;

import com.wordnik.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import net.woniper.board.domain.type.AuthorityType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by woniper on 15. 1. 28..
 */
@Getter @Setter
public class UserDto {

    @Size(min = 5) @NotNull private String username;
    @NotNull private String firstName;
    @NotNull private String lastName;
    @NotNull private String nickName;
    private AuthorityType authorityType;

    @ApiModel(value = "user Request")
    @Getter @Setter
    public static class Request extends UserDto {
        @Size(min = 5) @NotNull private String password;
    }

    @ApiModel(value = "user Response")
    @Getter @Setter
    public static class Response extends UserDto {
        private Long userId;
        private Date joinDate;
        private boolean active;
    }
}
