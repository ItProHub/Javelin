package site.itprohub.javelin.rest;

import site.itprohub.javelin.annotations.*;
import site.itprohub.javelin.context.JavelinContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.lang.annotation.*;

public class Router {

    public void registerRoutes(HttpServer server, Set<Class<?>> classes, JavelinContext context) {

        for (Class<?> clazz : classes) {
            if (!clazz.isAnnotationPresent(RestController.class))
                continue;
                
            Object controllerInstance = context.getBean(clazz);

            for (Method method : clazz.getDeclaredMethods()) {
                Annotation[] annotations = method.getAnnotations();

                for (Annotation annotation : annotations) {
                   HttpMethodMapping mapping = annotation.annotationType().getAnnotation(HttpMethodMapping.class);
                    if (mapping != null) {
                       String httpMethod = mapping.method();
                       
                       try{
                           String path = (String) annotation.annotationType().getMethod("value").invoke(annotation);
                           registerHandler(server, path, httpMethod, controllerInstance, method);
                       } catch (Exception e) {
                           e.printStackTrace(); 
                       }

                    }
                }
            }
        }
       
    }


    private void registerHandler(HttpServer server, String path, String expectedMethod, Object controllerInstance, Method method) {
        server.createContext(path, exchange -> {
            // 校验请求方法的合法性
            if (!exchange.getRequestMethod().equalsIgnoreCase(expectedMethod)) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            try {
                Object result = method.invoke(controllerInstance);

                byte[] response = result.toString().getBytes();

                exchange.sendResponseHeaders(200, response.length);
                try (OutputStream os = exchange.getResponseBody()){
                    os.write(response);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(500, 0);
            }
        });
    }



}
