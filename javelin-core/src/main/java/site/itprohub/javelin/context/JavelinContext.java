package site.itprohub.javelin.context;

import site.itprohub.javelin.annotations.Inject;

import java.lang.reflect.Constructor;
import java.util.*;

public class JavelinContext {

    private final Map<Class<?>, Object> singletonMap = new HashMap<>();

    public <T> T getBean(Class<T> clazz) {
        try {
            if (singletonMap.containsKey(clazz)) {
                return (T) singletonMap.get(clazz);
            }

            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            Constructor<?> injectConstructor = null;

            for (Constructor<?> constructor : constructors) {
                if (constructor.isAnnotationPresent(Inject.class)) {
                    injectConstructor = constructor;
                    break;
                }
            }

            if (injectConstructor == null) {
                injectConstructor = clazz.getDeclaredConstructor(); // fallback to default
            }

            List<Object> args = new ArrayList<>();
            for (Class<?> paramType : injectConstructor.getParameterTypes()) {
                args.add(getBean(paramType)); // 递归构建依赖
            }

            T instance = (T) injectConstructor.newInstance(args.toArray());
            singletonMap.put(clazz, instance);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate: " + clazz.getName(), e);
        }
    }
}
