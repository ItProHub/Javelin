package site.itprohub.javelin.rest;

import site.itprohub.javelin.annotations.*;
import site.itprohub.javelin.context.JavelinContext;
import site.itprohub.javelin.http.Pipeline.NHttpContext;
import site.itprohub.javelin.startup.AppStartup;
import site.itprohub.javelin.utils.EnvUtils;
import site.itprohub.javelin.utils.UrlExtensions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.reflections.Reflections;
import java.lang.annotation.*;

public class Router {

    private static final JavelinContext JAVELIN_CONTEXT;

    private final List<RouteDefinition> dynamicRoutes = new ArrayList<>();

    static { // 静态初始化块，在类加载时执行
        JAVELIN_CONTEXT = new JavelinContext(); // 创建JavelinContext实例        
    }

    /**
     * 注册路由
     * 
     * @param server  HttpServer 实例
     * @param classes 包含控制器类的集合
     * @param context JavelinContext 实例
     **/
    public void registerRoutes() {

        Reflections reflections = new Reflections(EnvUtils.ApplicationName);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(RestController.class);

        for (Class<?> clazz : controllers) {
            if (!clazz.isAnnotationPresent(RestController.class))
                continue;

            for (Method method : clazz.getDeclaredMethods()) {
                Annotation[] annotations = method.getAnnotations();

                for (Annotation annotation : annotations) {
                    HttpMethodMapping mapping = annotation.annotationType().getAnnotation(HttpMethodMapping.class);
                    if (mapping != null) {
                        String httpMethod = mapping.method();

                        try {
                            String path = (String) annotation.annotationType().getMethod("value").invoke(annotation);
                            Pattern pathPattern = UrlExtensions.compilePathPattern(path);
                            List<String> pathVaribleNames = UrlExtensions.extractPathVaribleNames(path);
                            dynamicRoutes.add(new RouteDefinition(httpMethod, path, pathPattern, pathVaribleNames, clazz, method));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }

        if(AppStartup.STARTUP_OPTION.showHomePage) {
            try{
                dynamicRoutes.add(new RouteDefinition("GET", "/", Pattern.compile("/"), List.of(), DefaultRootHandler.class, DefaultRootHandler.class.getDeclaredMethod("handle", NHttpContext.class)));
            } catch (Exception e) {
               throw new RuntimeException(e); 
            }
        }
    }


    private RouteDefinition findMatchRoute(String requestPath) {
        return dynamicRoutes.stream()
            .filter(route -> matchRoute(requestPath, route))
            .findFirst()
            .orElse(null);
    }

    private boolean matchRoute(String requestPath, RouteDefinition route) {
       Matcher matcher = route.pathPattern.matcher(requestPath);
       return matcher.matches();
    }


    public void handle(NHttpContext httpContext) throws Exception {
        String requestPath = httpContext.getPath();

        RouteDefinition matchedRoute = findMatchRoute(requestPath);

        if (matchedRoute == null) {
            // 匹配不到404
            httpContext.response.setStatus(404);
            httpContext.response.getWriter().write("page not found");
            return;
        }

        // if(matchedRoute.controller == null) {
        //     matchedRoute.action.invoke(null, new Object[]{httpContext});
        //     return;
        // }

        // 延迟实例化controller
        Object controllerInstance = JAVELIN_CONTEXT.getBean(matchedRoute.clazz);
        matchedRoute.setController(controllerInstance);

        httpContext.setRouteDefinition(matchedRoute);
        try {
            ActionExecutor.INSTANCE.execute(httpContext);
        } catch (Exception e) {
            httpContext.lastException = e;
        } finally {
            // 释放资源
            httpContext.pipelineContext.dispose();

            JAVELIN_CONTEXT.callPreDestroy(controllerInstance);
        }
    }

}
