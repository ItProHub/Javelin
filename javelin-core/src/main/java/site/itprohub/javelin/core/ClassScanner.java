package site.itprohub.javelin.core;

import org.reflections.Reflections;
import java.util.Set;
import site.itprohub.javelin.annotations.RestController;

/**
 * 类扫描器
 */
public class ClassScanner {
    
    public static Set<Class<?>> scan(String packageName) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getTypesAnnotatedWith(RestController.class);
    }


}
