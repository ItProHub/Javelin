package site.itprohub.javelin.http.Pipeline;

import java.io.IOException;

import site.itprohub.javelin.dto.BaseUserInfo;
import site.itprohub.javelin.log.OprLog;
import site.itprohub.javelin.log.OprLogScope;
import site.itprohub.javelin.rest.RouteDefinition;
import site.itprohub.javelin.web.security.Auth.NPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class NHttpContext {

    public HttpPipelineContext pipelineContext;

    public OprLogScope oprLogScope;

    public Exception lastException;

    public boolean skipAuthentication;

    public HttpServletRequest request;

    public HttpServletResponse response;

    public NPrincipal user;

    public boolean isAuthenticated() {
        if (user != null && user.ticket != null  && user.ticket.getUser() != null) {
            return true;
        }
        return false;
    }

    public NHttpContext() {
        this.pipelineContext = HttpPipelineContext.start(this);
    }

    public void setRouteDefinition(RouteDefinition routeDefinition) {
        this.pipelineContext.routeDefinition = routeDefinition; 
    }

    void setOprLogScope(OprLogScope oprLogScope) {
        this.oprLogScope = oprLogScope;
    }

    public void httpReply(int statusCode, String message) {
        try {
            this.response.setContentType("text/plain; chatset=UTF-8");
            this.response.setCharacterEncoding("UTF-8");
            this.response.setStatus(statusCode);
            this.response.getWriter().write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUserInfoToLog(BaseUserInfo userInfo) {
        OprLog log = oprLogScope.oprlog;
        log.userId = userInfo.getUserId();
        log.userName = userInfo.getUserName();
        log.userRole = userInfo.getUserRole();
    }

    public abstract String getPath();

    public abstract String getMethod();

}
