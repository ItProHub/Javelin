package site.itprohub.javelin.http.Pipeline;

import java.io.IOException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class HttpResponse {
    private final HttpServletResponse response;

    private final NHttpContext httpContext;

    public HttpResponse(HttpServletResponse response, NHttpContext httpContext) {
        this.response = response;
        this.httpContext = httpContext;
    }

    public void setStatus(int status) {
        response.setStatus(status);
    }

    public void setCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public void setHeader(String name, String value) {
        response.setHeader(name, value);
    }

    public void setContentType(String contentType) {
        response.setContentType(contentType);
    }

    public void setContentLength(int length) {
        response.setContentLength(length);
    }

    public void setCharacterEncoding(String encoding) {
        response.setCharacterEncoding(encoding);
    }

    public void write(String content) throws IOException {
        response.getWriter().write(content);
    }

    public void flush() throws IOException {
        response.getWriter().flush();
    }

    public void end() {
        httpContext.pipelineContext.completeRequest();
    }

    public void close() throws IOException {
        response.getWriter().close();
    }
}
