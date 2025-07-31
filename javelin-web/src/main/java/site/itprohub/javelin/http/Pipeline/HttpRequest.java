package site.itprohub.javelin.http.Pipeline;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;

public class HttpRequest {
    private final HttpServletRequest request;

    private final NHttpContext httpContext;

    public HttpRequest(HttpServletRequest request, NHttpContext httpContext) {
        this.request = request;
        this.httpContext = httpContext;
    }

    public NHttpContext getHttpContext() {
        return httpContext;
    }

    public HttpServletRequest getOriginalRequest() {
        return request;
    }

    public String getMethod() {
        return request.getMethod();
    }

    public String getRequestURI() {
        return request.getRequestURI();
    }

    public String queryString(String name) {
        return request.getParameter(name);
    }

    public String getHeader(String name) {
        return request.getHeader(name);
    }

    public String cookie(String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (name.equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        return null;
    }


    public ServletInputStream  getInputStream() throws IOException {
        return request.getInputStream();
    }

    public Map<String, String[]> getParameterMap() {
        return request.getParameterMap();
    }

}
