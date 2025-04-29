package site.itprohub.javelin.http.Pipeline;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import site.itprohub.javelin.log.OprLogScope;
import site.itprohub.javelin.rest.RouteDefinition;

public class NHttpContext {
    public HttpExchange exchange;

    public HttpPipelineContext pipelineContext;

    public OprLogScope oprLogScope;

    public Exception lastException;

    public boolean skipAuthentication;

    public NHttpContext(HttpExchange exchange, RouteDefinition routeDefinition) {
        this.exchange = exchange;
        this.pipelineContext = HttpPipelineContext.start(this);
        this.pipelineContext.routeDefinition = routeDefinition;
    }

    void setOprLogScope(OprLogScope oprLogScope) {
        this.oprLogScope = oprLogScope;
    }

    public void httpReply(int statusCode, String message) {
        try {
            this.exchange.sendResponseHeaders(statusCode, 0);
            this.exchange.getResponseBody().write(message.getBytes());
            this.exchange.close(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    

}
