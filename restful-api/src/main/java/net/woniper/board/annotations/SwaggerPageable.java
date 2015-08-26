package net.woniper.board.annotations;

import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;

import java.lang.annotation.*;

/**
 * Created by woniper on 15. 8. 26..
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SwaggerPageable {

}
