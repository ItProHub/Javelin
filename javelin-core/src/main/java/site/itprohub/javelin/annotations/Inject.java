package site.itprohub.javelin.annotations;

import java.lang.annotation.*;

@Target({ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {}
