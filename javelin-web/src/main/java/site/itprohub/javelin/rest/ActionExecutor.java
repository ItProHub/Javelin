package site.itprohub.javelin.rest;

import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import site.itprohub.javelin.annotations.AllowAnonymous;
import site.itprohub.javelin.annotations.parameter.FromBody;
import site.itprohub.javelin.annotations.parameter.FromQuery;
import site.itprohub.javelin.annotations.parameter.FromRoute;
import site.itprohub.javelin.http.Pipeline.AbortRequestException;
import site.itprohub.javelin.http.Pipeline.NHttpApplication;
import site.itprohub.javelin.http.Pipeline.NHttpContext;
import site.itprohub.javelin.utils.StringExtensions;

public class ActionExecutor {
    public static final ActionExecutor INSTANCE = new ActionExecutor();

    private final Gson gson = new Gson();

    public void execute(NHttpContext context) throws Exception {
        NHttpApplication app = NHttpApplication.INSTANCE;

        preHandle(context);
        try{
            app.beginRequest(context);

            app.authenticateRequest(context);

            app.postAuthenticateRequest(context);

            app.resolveRequestCache(context);

            app.authorizeRequest(context);

            handlerRequest(context);
        } catch ( AbortRequestException e) {
            // 提前结束请求，啥也不干了
        } catch (Exception e) {
            // 获取原始异常
            Throwable cause = e.getCause();
            context.pipelineContext.setException((Exception)cause);
            app.onError(context);
        } finally {
            app.endRequest(context);
        }

    }

    private void preHandle(NHttpContext context) throws Exception {
        RouteDefinition routeDefinition = context.pipelineContext.routeDefinition;

        if (context.request.getRequestURI().equals("/")) {
            context.skipAuthentication = true;
        }
        // 反射获取所请求方法所在Controller和action的注解信息
        // 如果Controller类上有AllowAnonymous注解，则不需要验证token
        if (routeDefinition.controller.getClass().isAnnotationPresent(AllowAnonymous.class)) { 
            context.skipAuthentication = true;
        }

         // 检查方法上是否有 @AllowAnonymous 注解
         if (routeDefinition.action.isAnnotationPresent(AllowAnonymous.class)) {
            context.skipAuthentication = true;
        }
    }


    private void handlerRequest(NHttpContext context) throws Exception {
        RouteDefinition route = context.pipelineContext.routeDefinition;
        HttpServletResponse response = context.response;

        // 校验请求方法的合法性
        if (!route.httpMethod.equalsIgnoreCase(context.getMethod())) {
            response.setStatus(405);
            return;
        }
        
        Matcher matcher = route.pathPattern.matcher(context.getPath());
        if (!matcher.matches()) {
            throw new RuntimeException("路径未匹配成功：" + context.getPath());
        }
        // 解析路径参数并填充到参数列表中
        Map<String, String> pathParams = new HashMap<>();
        for (String name : route.pathVaribleNames) {
            pathParams.put(name, matcher.group(name));
        }

        // 获取方法参数
        Object[] args = resolveMethodParameters(context, route, pathParams);
        // 调用方法并获取返回值
        Object result = route.action.invoke(route.controller, args);

        // 获取返回值的类型
        Class<?> returnType = route.action.getReturnType();

        String responseBody;
        if(returnType == String.class || returnType == void.class || returnType == int.class) {
            responseBody = result.toString();
            context.response.addHeader("Content-Type", "text/plain; charset=UTF-8");
        } else {
            responseBody = gson.toJson(result);
            context.response.addHeader("Content-Type", "application/json; charset=UTF-8");
        }

        byte[] responseBytes = responseBody.getBytes(StandardCharsets.UTF_8);

        response.setStatus(200);
        response.setContentLength(responseBytes.length);
        response.getWriter().write(responseBody);
        response.getWriter().flush();
        
    }


    /**
     * 解析方法参数
     * 
     * @param method   方法
     * @param exchange HttpExchange 实例
     * @return 参数数组
     * @throws Exception 异常
     **/
    private Object[] resolveMethodParameters(NHttpContext context, RouteDefinition route, Map<String, String> pathVariables) throws Exception {
        Parameter[] parameters = route.action.getParameters();

        // 读取请求体内容
        String bodyString = null;

        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            if (parameter.isAnnotationPresent(FromQuery.class)) {
                // 解析 URL 中的参数
                Map<String, String[]> queryMap = context.request.getParameterMap();
                // Map<String, String> queryMap = UrlExtensions.parseQueryParams(context.request.getRequestURI().getRawQuery());

                String key = parameter.getName();
                String value = queryMap.get(key)[0];
                args[i] = StringExtensions.convertTo(value, parameter.getType());
            } else if (parameter.isAnnotationPresent(FromBody.class)) {
                if (bodyString == null) {
                    bodyString = new String(context.request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                }
                // 从请求体中读取数据并转换为对象
                // 这里需要根据实际情况实现从请求体中读取数据的逻辑
                args[i] = new Gson().fromJson(bodyString, parameter.getType());
            } else if (parameter.isAnnotationPresent(FromRoute.class)) {
                // 获取参数名称
                String name = parameter.getName();
                // 例如，从请求路径中提取参数值
                String value = pathVariables.get(name);
                args[i] = StringExtensions.convertTo(value, parameter.getType());
            } else {
                if (parameter.getType() == NHttpContext.class) {
                    args[i] = context; // 直接传递 context 对象
                    continue; // 跳过其他参数的处理
                }
                args[i] = null; // 暂不支持其他来源
            }
        }

        return args;
    }


}
