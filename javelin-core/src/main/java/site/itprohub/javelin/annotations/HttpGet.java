package site.itprohub.javelin.annotations;

import java.lang.annotation.*;

@HttpMethodMapping(method = "GET")
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpGet {
}
