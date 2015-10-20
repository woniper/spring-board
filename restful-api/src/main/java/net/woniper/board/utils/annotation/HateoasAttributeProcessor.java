package net.woniper.board.utils.annotation;

import org.springframework.core.MethodParameter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Created by woniper on 2015. 10. 3..
 */
public class HateoasAttributeProcessor implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getMethodAnnotation(HateoasAttribute.class) != null;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        if(returnValue instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity)returnValue;
            if(responseEntity.getBody() instanceof ResourceSupport) {
                HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
                String url = request.getRequestURL().toString();
                ResourceSupport resourceSupport = (ResourceSupport) responseEntity.getBody();
                Link getLink =      new Link(url + "/{id}", "GET");
                Link postLink =     new Link(url, "POST");
                Link putLink =      new Link(url, "PUT");
                Link deleteLink =   new Link(url, "DELETE");
                resourceSupport.add(Arrays.asList(getLink, postLink, putLink, deleteLink));
            }
        }
    }
}
