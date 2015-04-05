package net.woniper.board.errors;

import lombok.Data;

/**
 * Created by woniper on 15. 2. 9..
 */
@Data
public class ErrorResponse {
    private int status;
    private int code;
    private String message;
    private String developerMassage;
    private String moreInfo;
}
