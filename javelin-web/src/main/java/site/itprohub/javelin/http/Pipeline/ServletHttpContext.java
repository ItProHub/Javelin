package site.itprohub.javelin.http.Pipeline;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServletHttpContext extends NHttpContext {    

    public ServletHttpContext(HttpServletRequest request, HttpServletResponse response) {
        super();
        this.request = request;
        this.response = response;
    }

    // 提供 getHeader, getMethod, getPath, getBodyStream 等
    public String getHeader(String name) { 
        return request.getHeader(name); 
    }

    public String getMethod() { 
        return request.getMethod(); 
    }

    public String getPath() { 
        return request.getRequestURI();
    }
}
