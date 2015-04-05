package net.woniper.board.errors;

import net.woniper.board.errors.support.DuplicateNickNameException;
import net.woniper.board.errors.support.DuplicateUsernameException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * Created by woniper on 15. 2. 9..
 */
@ControllerAdvice
public class ErrorControllerAdvice {

    @Autowired private HttpServletRequest request;

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateUsernameException.class)
    public @ResponseBody ErrorResponse duplicateUsernameException(DuplicateUsernameException exception, Principal principal) {
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
    public @ResponseBody ErrorResponse duplicateNickNameException(DuplicateNickNameException exception, Principal principal) {
        printLog(exception, principal);
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse error = new ErrorResponse();
        error.setStatus(status.value());
        error.setMessage(status.getReasonPhrase());
        error.setDeveloperMassage(exception.getNickName() + " Duplicate nickName");
        return error;
    }

    private void printLog(RuntimeException exception, Principal principal) {
        String username = StringUtils.EMPTY;
        if(principal != null) {
            username = principal.getName();
        }
        StackTraceElement[] elements = exception.getStackTrace();
        StackTraceElement element = elements[elements.length - 1];

        String logMsg = String.format("username : {%s}, ClassName : {%s}, MethodName : {%s}, LineNumber : {%d}",
                username, element.getClassName(), element.getMethodName(), element.getLineNumber());

        System.out.println(logMsg);
    }


}
