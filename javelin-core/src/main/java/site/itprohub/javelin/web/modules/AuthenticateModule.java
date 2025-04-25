package site.itprohub.javelin.web.modules;

import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;

import site.itprohub.javelin.annotations.AllowAnonymous;
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

        HttpExchange exchange = httpContext.exchange;
        String authorizationHeader = exchange.getRequestHeaders().getFirst("Authorization");

        if(authorizationHeader == null || !authorizationHeader.equals("Bearer valid_token")){
            exchange.getResponseHeaders().set("X-Response-ErrorCode", "NotLogin");
            exchange.sendResponseHeaders(401, 0); // 未登录，返回401状态码
            try (OutputStream os = exchange.getResponseBody()){
                os.write("Please login.".getBytes());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage()); // 打印错误信息
            }
            exchange.getResponseBody().close();

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
