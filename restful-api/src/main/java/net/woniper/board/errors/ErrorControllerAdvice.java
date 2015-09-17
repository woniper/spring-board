package net.woniper.board.errors;

import lombok.extern.slf4j.Slf4j;
import net.woniper.board.errors.support.BoardNotFoundException;
import net.woniper.board.errors.support.NickNameDuplicateException;
import net.woniper.board.errors.support.UserNotFoundException;
import net.woniper.board.errors.support.UsernameDuplicateException;
import org.apache.commons.lang3.StringUtils;
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
@Slf4j
public class ErrorControllerAdvice {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UsernameDuplicateException.class)
    public ErrorResponse duplicateUsernameException(UsernameDuplicateException exception, Principal principal) {
        printLog(exception, principal);
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse error = new ErrorResponse();
        error.setStatus(status.value());
        error.setMessage(status.getReasonPhrase());
        error.setDeveloperMassage(exception.getUsername() + " Duplicate username");
        return error;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(NickNameDuplicateException.class)
    public ErrorResponse duplicateNickNameException(NickNameDuplicateException exception, Principal principal) {
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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {UserNotFoundException.class})
    public ErrorResponse userNotFoundException(UserNotFoundException exception, Principal principal) {
        printLog(exception, principal);
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse error = new ErrorResponse();
        error.setStatus(status.value());
        error.setMessage(status.getReasonPhrase());
        error.setDeveloperMassage("Not Found User Name Or Id : " + exception.getUser());
        return error;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {BoardNotFoundException.class})
    public ErrorResponse boardNotFoundException(BoardNotFoundException exception, Principal principal) {
        printLog(exception, principal);
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse error = new ErrorResponse();
        error.setStatus(status.value());
        error.setMessage(status.getReasonPhrase());
        error.setDeveloperMassage("Board Not Found Exception");
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

        log.error("username : {%s}, error : {%s}",username, exception.getMessage());
    }


}
