package site.itprohub.javelin.http.Pipeline;

import site.itprohub.javelin.base.BasePipelineContext;
import site.itprohub.javelin.log.OprLogScope;
import site.itprohub.javelin.rest.RouteDefinition;

public class HttpPipelineContext extends BasePipelineContext {

    public NHttpContext httpContext;

    public RouteDefinition routeDefinition;

    private static final ThreadLocal<HttpPipelineContext> s_local = new ThreadLocal<>();


    public static HttpPipelineContext start(NHttpContext httpContext) {
        HttpPipelineContext pipelineContext = new HttpPipelineContext(httpContext);

        s_local.set(pipelineContext);

        OprLogScope oprLogScope = OprLogScope.start(httpContext.pipelineContext);
        pipelineContext.httpContext.setOprLogScope(oprLogScope);

        return pipelineContext;
    }

    public static HttpPipelineContext get(){
        HttpPipelineContext ctx = s_local.get();
        if (ctx != null && ctx.httpContext == null) {
            // 这里还要检查它的有效性

            s_local.set(null);
            return null;
        }

        return ctx;

    }

    public static HttpPipelineContext current() {
        HttpPipelineContext ctx = HttpPipelineContext.get();
        if (ctx == null) {
            throw new IllegalStateException("当前请求没有关联到一个PipelineContext实例");
        }
        return null;
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

        s_local.set(null);

    }
    
}
