package site.itprohub.javelin.http.Pipeline;

import java.io.IOException;

import site.itprohub.javelin.log.OprLogScope;
import site.itprohub.javelin.rest.RouteDefinition;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class NHttpContext {

    public HttpPipelineContext pipelineContext;

    public OprLogScope oprLogScope;

    public Exception lastException;

    public boolean skipAuthentication;

    public HttpServletRequest request;

    public HttpServletResponse response;

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
            this.response.setStatus(statusCode);
            this.response.getWriter().write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract String getPath();

    public abstract String getMethod();

}
