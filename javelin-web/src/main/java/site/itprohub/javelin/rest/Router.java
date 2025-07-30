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

            // 处理类上的 @Route
            String classPrefix = "";
            if (clazz.isAnnotationPresent(Route.class)) {
                classPrefix = clazz.getAnnotation(Route.class).value();
            }

            for (Method method : clazz.getDeclaredMethods()) {

                // ✅ 必须有 @Route 注解才能注册
                if (!method.isAnnotationPresent(Route.class)) {
                    continue;
                }

                 // ✅ 查找方法上的 HTTP 方法注解（@GET/@POST 等）
                String httpMethod = null;
                for (Annotation annotation : method.getAnnotations()) {
                    HttpMethodMapping mapping = annotation.annotationType().getAnnotation(HttpMethodMapping.class);
                    if (mapping != null) {
                        httpMethod = mapping.method(); // e.g., "GET", "POST"
                        break; // 一般只有一个
                    }
                }

                // ✅ 如果没有 GET/POST 等注解就跳过
                if (httpMethod == null)
                    continue;

                try {
                    if (!method.isAnnotationPresent(Route.class)) {
                        throw new IllegalStateException("缺少 @Route 注解: " + clazz.getName() + "#" + method.getName());
                    }
                    String methodPath = method.getAnnotation(Route.class).value();
                    // 合并类前缀和方法路径
                    String path = classPrefix + methodPath;

                    Pattern pathPattern = UrlExtensions.compilePathPattern(path);
                    List<String> pathVaribleNames = UrlExtensions.extractPathVaribleNames(path);
                    dynamicRoutes.add(new RouteDefinition(httpMethod, path, pathPattern, pathVaribleNames, clazz, method));

                } catch (Exception e) {
                    e.printStackTrace();
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
