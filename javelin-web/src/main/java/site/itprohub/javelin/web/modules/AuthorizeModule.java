package site.itprohub.javelin.web.modules;

import java.io.IOException;

import site.itprohub.javelin.http.Pipeline.NHttpContext;
import site.itprohub.javelin.http.Pipeline.NHttpModule;

public class AuthorizeModule extends NHttpModule {
    @Override
    public int getOrder(){
        return -9;
    }

    @Override
    public void authorizeRequest(NHttpContext httpContext) throws IOException {
        // 验证用户是否登录
        if ( authorizeCheck(httpContext) ){
            return;
        }
        // 如果用户未登录，重定向到登录页面或返回错误信息
        // 如果用户已登录，继续执行下一个中间件或处理程序
    }
    
    private boolean authorizeCheck(NHttpContext httpContext) throws IOException {
        // 验证用户是否登录的逻辑
        // 可以从会话、Cookie、Token等获取用户信息
        // 返回true表示用户已登录，返回false表示用户未登录

        if (httpContext.skipAuthentication) {
            return true;
        }

        if (httpContext.isAuthenticated()) {
            return true;
        }

        httpContext.response.setHeader("X-Response-ErrorCode", "NotLogin");
        httpContext.response.setStatus(401); // 未登录，返回401状态码
        httpContext.response.write("Please login."); // 未登录，返回错误信息
        httpContext.response.close(); // 关闭输出流

        httpContext.pipelineContext.completeRequest();
        return false;


    }

}
