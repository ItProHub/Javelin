package site.itprohub.javelin.web.modules;

import site.itprohub.javelin.http.Pipeline.NHttpContext;
import site.itprohub.javelin.http.Pipeline.NHttpModule;
import site.itprohub.javelin.web.security.Auth.AuthenticationManager;

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

        AuthenticationManager.authenticationUser(httpContext);    
    }

    @Override
    public void endRequest(NHttpContext httpContext) {
       // 在oprlog里面记录当前用户信息
       // IUsserInfo user = httpContext.getUserInfo();
       // httpContext.setUserInfoToOprLog(user); 
    }
}
