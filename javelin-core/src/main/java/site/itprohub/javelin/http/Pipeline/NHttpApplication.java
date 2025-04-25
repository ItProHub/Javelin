package site.itprohub.javelin.http.Pipeline;

import java.util.List;

public class NHttpApplication {
    private List<NHttpModule> modules = null;

    public static NHttpApplication INSTANCE;

    private NHttpApplication() {
        modules = NHttpModuleFactory.createModules();

        for (NHttpModule module : modules) {
            module.init();
        }
    }

    public static NHttpApplication start() {
        if (INSTANCE != null) {
            throw new IllegalStateException("此方法不允许多次调用！");
        }

        NHttpApplication app = new NHttpApplication();

        INSTANCE = app;

        return app;
    }

    public void showModules() {
        System.out.println("----------------------- HttpModules ---------------------------- ");
        int i = 0;
        for (NHttpModule module : modules) {
            i++;
            System.out.println(i + ": " + module.getClass().getName() + " loaded, Order: " + module.getOrder()); 
        }
    }

    public void beginRequest(NHttpContext httpContext) {
        for (NHttpModule module : modules) {
            module.beginRequest(httpContext);
        }
    }

    public void onError(NHttpContext httpContext) {
        for (NHttpModule module : modules) {
            module.onError(httpContext);
        } 
    }

    public void authenticateRequest(NHttpContext httpContext) throws Exception {
        // 如果skipAuthentication为true，则跳过认证
        if (httpContext.skipAuthentication) 
            return;        

        for (NHttpModule module : modules) {
            module.authenticateRequest(httpContext);
        }
    }

    public void postAuthenticateRequest(NHttpContext httpContext) throws Exception {
        // 如果skipAuthentication为true，则跳过认证
        if (httpContext.skipAuthentication) 
            return;        

        for (NHttpModule module : modules) {
            module.postAuthenticateRequest(httpContext);
        }
    }

    public void resolveRequestCache(NHttpContext httpContext) throws Exception {
        for (NHttpModule module : modules) {
            module.resolveRequestCache(httpContext);
        }
    }

    public void endRequest(NHttpContext httpContext) {
        for (NHttpModule module : modules) {
            module.endRequest(httpContext);
        }
    }
}
