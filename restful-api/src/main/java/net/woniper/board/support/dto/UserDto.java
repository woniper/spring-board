package net.woniper.board.support.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by woniper on 15. 1. 28..
 */
@Data
public class UserDto {

    @Size(min = 5) @NotNull private String username;
    @NotNull private String firstName;
    @NotNull private String lastName;
    @NotNull private String nickName;
    private boolean admin = false;

    @Data
    public static class Request extends UserDto {
        @Size(min = 5) @NotNull private String password;
    }

    @Data
    public static class Response extends UserDto {
        private Long userId;
        private Date joinDate;
        private boolean active = true;
    }
}
