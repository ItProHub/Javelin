package site.itprohub.javelin.annotations;

import java.lang.annotation.*;

@HttpMethodMapping(method = "POST")
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostMapping {
    String value();
}
