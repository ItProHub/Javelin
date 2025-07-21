package site.itprohub.javelin.http.Pipeline;

/**
 * 类似 ASP.NET 中的 IHttpModule
 */
public abstract class NHttpModule {
    
    /**
     * NHttpModule[]中的排序位置，也可以理解为执行次序。
     * @return 模块执行顺序
     */
    public int getOrder() {
        return 2000;
    }

    /**
     * 模块初始化，只调用一次。
     */
    public void init() {
    }

    /**
     * BeginRequest
     * @param httpContext HTTP上下文对象
     */
    public void beginRequest(NHttpContext httpContext) {
    }

    /**
     * AuthenticateRequest
     * @param httpContext HTTP上下文对象
     */
    public void authenticateRequest(NHttpContext httpContext) throws Exception {
    }

    /**
     * PostAuthenticateRequest
     * @param httpContext HTTP上下文对象
     */
    public void postAuthenticateRequest(NHttpContext httpContext) {
    }

    /**
     * AuthorizeRequest
     * @param httpContext HTTP上下文对象
     */
    public void authorizeRequest(NHttpContext httpContext) throws Exception {
    }

    /**
     * ResolveRequestCache
     * @param httpContext HTTP上下文对象
     */
    public void resolveRequestCache(NHttpContext httpContext) {
    }

    /**
     * PreFindAction
     * @param httpContext HTTP上下文对象
     */
    public void preFindAction(NHttpContext httpContext) {
    }

    /**
     * PostFindAction
     * @param httpContext HTTP上下文对象
     */
    public void postFindAction(NHttpContext httpContext) {
    }

    /**
     * PreRequestExecute
     * @param httpContext HTTP上下文对象
     */
    public void preRequestExecute(NHttpContext httpContext) {
    }

    /**
     * PostRequestExecute
     * @param httpContext HTTP上下文对象
     */
    public void postRequestExecute(NHttpContext httpContext) {
    }

    /**
     * UpdateRequestCache
     * @param httpContext HTTP上下文对象
     */
    public void updateRequestCache(NHttpContext httpContext) {
    }

    /**
     * EndRequest
     * @param httpContext HTTP上下文对象
     */
    public void endRequest(NHttpContext httpContext) {
    }

    /**
     * 异常处理。
     * @param httpContext HTTP上下文对象
     */
    public void onError(NHttpContext httpContext) {
    }
}