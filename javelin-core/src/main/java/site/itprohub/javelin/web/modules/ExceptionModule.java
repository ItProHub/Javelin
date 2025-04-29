package site.itprohub.javelin.web.modules;

import java.io.IOException;

import site.itprohub.javelin.base.exceptions.HttpException;
import site.itprohub.javelin.http.Pipeline.NHttpContext;
import site.itprohub.javelin.http.Pipeline.NHttpModule;

public class ExceptionModule extends NHttpModule {
    @Override
    public int getOrder(){
        return 9999;
    }

    @Override
    public void onError(NHttpContext httpContext) {
        Exception ex = httpContext.lastException;
        // 如果请求没有异常，则不处理
        if (ex == null) {
            return;
        }

        if (ex instanceof HttpException) {
            HttpException httpEx = (HttpException) ex;
            httpContext.httpReply(httpEx.getErrorCode(), httpEx.getMessage());
            return;
        } else {
            httpContext.httpReply(500, ex.getMessage());
        }
    }
}