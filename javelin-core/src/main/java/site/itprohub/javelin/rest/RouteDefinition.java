package site.itprohub.javelin.rest;

import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;

public class RouteDefinition {
    public String httpMethod; // GEt/POST/PUT/DELETE
    public String rawPath; // 如 /users/{id}
    public Pattern pathPattern; //  编译后的正则，如 /users/(?<id>[^/]+)
    public List<String> pathVaribleNames; // 如 ["id"]
    public Method action; // 处理方法
    public Object controller;
    public Class<?> clazz;

    public RouteDefinition(String httpMethod, String rawPath, Pattern pathPattern, List<String> pathVariableNames, Class<?> clazz, Method method) {
        this.httpMethod = httpMethod;
        this.rawPath = rawPath;
        this.pathPattern = pathPattern;
        this.pathVaribleNames = pathVariableNames;
        this.clazz = clazz;
        this.action = method;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }
}
