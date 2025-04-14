package site.itprohub.javelin.annotations;

import java.lang.annotation.*;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpMethodMapping {
    String method();
}
