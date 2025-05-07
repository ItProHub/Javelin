package site.itprohub.javelin.web.modules;

import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;

import site.itprohub.javelin.http.Pipeline.NHttpContext;
import site.itprohub.javelin.http.Pipeline.NHttpModule;
import site.itprohub.javelin.rest.RouteDefinition;

public class AuthenticateModule extends NHttpModule {

    @Override
    public int getOrder() {
        return -10;
    }

    @Override
    public void authenticateRequest(NHttpContext httpContext) throws Exception {
        if (httpContext == null) { // 如果httpContext为空，则抛出异常
            throw new IllegalArgumentException("httpContext cannot be null"); // 抛出异常
        }

        RouteDefinition routeDefinition = httpContext.pipelineContext.routeDefinition;

        if (routeDefinition == null) {
            return;
        }

        String authorizationHeader = httpContext.request.getHeader("Authorization");

        if(authorizationHeader == null || !authorizationHeader.equals("Bearer valid_token")){
            httpContext.response.setHeader("X-Response-ErrorCode", "NotLogin");
            httpContext.response.setStatus(401); // 未登录，返回401状态码
            httpContext.response.getWriter().write("Please login."); // 未登录，返回错误信息
            httpContext.response.getWriter().close(); // 关闭输出流

            httpContext.pipelineContext.completeRequest();
            return;
        }
    }

    @Override
    public void endRequest(NHttpContext httpContext) {
       // 在oprlog里面记录当前用户信息
       // IUsserInfo user = httpContext.getUserInfo();
       // httpContext.setUserInfoToOprLog(user); 
    }
}
