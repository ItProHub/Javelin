package site.itprohub.javelin.context;

import site.itprohub.javelin.annotations.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

public class JavelinContext {

    private final Map<Class<?>, Object> singletonMap = new HashMap<>();

    public <T> T getBean(Class<T> clazz) {
        if (singletonMap.containsKey(clazz)) {
            return (T) singletonMap.get(clazz);
        }

        T instance = (T) createBean(clazz);
        singletonMap.put(clazz, instance);
        return instance;
        
    }

    public <T> T createBean(Class<T> clazz) {
        try {
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
            callPostConstruct(instance); // 调用@PostConstruct方法            
            return instance;
        }  catch (Exception e) {
            throw new RuntimeException("Failed to instantiate: " + clazz.getName(), e); 
        }
    }

    private void callPostConstruct(Object instance) throws Exception {
        Method postConstructMethod = findPostConstructMethod(instance.getClass());
        if (postConstructMethod != null) {
            postConstructMethod.invoke(instance);
        }
    }

    private Method findPostConstructMethod(Class<?> clazz) {
        for(Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                return method;
            }
        }
        return null;
    }

    public void callPreDestroy(Object instance) {
        Method preDestroyMethod = findPreDestroyMethod(instance.getClass());
        if (preDestroyMethod!= null) {
            try {
                preDestroyMethod.invoke(instance);
            } catch (Exception e) {
                throw new RuntimeException("Failed to call predestroy: " + preDestroyMethod.getName(), e); 
            }
        }
    }

    private Method findPreDestroyMethod(Class<?> clazz) {
        for(Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(PreDestroy.class)) {
                return method;
            }
        }
        return null;
    }

}
