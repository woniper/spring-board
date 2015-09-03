package net.woniper.board.errors;

import net.woniper.board.errors.support.DuplicateNickNameException;
import net.woniper.board.errors.support.DuplicateUsernameException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.security.Principal;

/**
 * Created by woniper on 15. 2. 9..
 */
@ControllerAdvice
@ResponseBody
public class ErrorControllerAdvice {

    private final Log log = LogFactory.getLog(ErrorControllerAdvice.class);

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateUsernameException.class)
    public ErrorResponse duplicateUsernameException(DuplicateUsernameException exception, Principal principal) {
        printLog(exception, principal);
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse error = new ErrorResponse();
        error.setStatus(status.value());
        error.setMessage(status.getReasonPhrase());
        error.setDeveloperMassage(exception.getUsername() + " Duplicate username");
        return error;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateNickNameException.class)
    public ErrorResponse duplicateNickNameException(DuplicateNickNameException exception, Principal principal) {
        printLog(exception, principal);
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse error = new ErrorResponse();
        error.setStatus(status.value());
        error.setMessage(status.getReasonPhrase());
        error.setDeveloperMassage(exception.getNickName() + " Duplicate nickName");
        return error;
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse accessDeniedException(AccessDeniedException exception, Principal principal) {
        printLog(exception, principal);
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        ErrorResponse error = new ErrorResponse();
        error.setStatus(status.value());
        error.setMessage(status.getReasonPhrase());
        error.setDeveloperMassage(exception.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public @ResponseBody ErrorResponse exception(Exception exception, Principal principal) {
        printLog(exception, principal);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse error = new ErrorResponse();
        error.setStatus(status.value());
        error.setMessage(status.getReasonPhrase());
        error.setDeveloperMassage(exception.getMessage());
        return error;
    }

    private void printLog(Exception exception, Principal principal) {
        String username = StringUtils.EMPTY;
        if(principal != null) {
            username = principal.getName();
        }

        String logMsg = String.format("username : {%s}, error{%s}",username, exception.getMessage());
        log.error(logMsg);
    }


}
