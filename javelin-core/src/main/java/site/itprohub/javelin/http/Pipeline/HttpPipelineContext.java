package site.itprohub.javelin.http.Pipeline;

import java.util.Date;

import site.itprohub.javelin.log.OprLogScope;
import site.itprohub.javelin.rest.RouteDefinition;

public class HttpPipelineContext {

    // 操作id，唯一不重复
    public String processId;

    public Date startTime;

    public Date endTime;


    public NHttpContext httpContext;

    public RouteDefinition routeDefinition;

    public Exception lastException;

    public static HttpPipelineContext start(NHttpContext httpContext) {
        HttpPipelineContext pipelineContext = new HttpPipelineContext(httpContext);

        OprLogScope oprLogScope = OprLogScope.start(httpContext.pipelineContext);
        pipelineContext.httpContext.setOprLogScope(oprLogScope);

        return pipelineContext;
    }

    public void setRouteDefinition(RouteDefinition routeDefinition) {
        this.routeDefinition = routeDefinition;
    }

    private HttpPipelineContext(NHttpContext httpContext) {
        if (httpContext == null) {
            throw new IllegalArgumentException("httpContext cannot be null");
        }
        this.httpContext = httpContext;

        httpContext.pipelineContext = this;

    }

    public void setException(Exception ex) {
        if( ex != null) {
            httpContext.lastException = ex;
        }
    }

    public void completeRequest() {
        throw new AbortRequestException();
    }

    public void dispose(){
        this.httpContext = null;
    }
}
