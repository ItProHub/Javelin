package site.itprohub.javelin.web.modules;


import java.util.Date;

import site.itprohub.javelin.http.Pipeline.NHttpContext;
import site.itprohub.javelin.http.Pipeline.NHttpModule;
import site.itprohub.javelin.log.OprLog;
import site.itprohub.javelin.log.OprLogScope;
import site.itprohub.javelin.rest.RouteDefinition;

public class OprLogModule extends NHttpModule {


    @Override
    public int getOrder() {
        return -9999;
    }

    @Override
    public void beginRequest(NHttpContext httpContext) {

        OprLogScope scope = httpContext.oprLogScope;

        scope.oprlog.rootId = httpContext.pipelineContext.processId;

    }

    @Override
    public void onError(NHttpContext httpContext) {
        httpContext.oprLogScope.setException(httpContext.lastException);
    }

    @Override
    public void endRequest(NHttpContext httpContext) {
        OprLogScope scope = httpContext.oprLogScope;

        OprLog log = scope.oprlog;

        log.endTime = new Date();

        RouteDefinition routeDefinition = httpContext.pipelineContext.routeDefinition;

        log.controller = routeDefinition.controller.getClass().getName();
        log.action = routeDefinition.action.getName();

        log.httpMethod = routeDefinition.httpMethod;
        log.url = routeDefinition.rawPath;
        log.UserAgent = httpContext.exchange.getRequestHeaders().getFirst("User-Agent");
        
        scope.saveOprLog(httpContext.pipelineContext);
    }

}
