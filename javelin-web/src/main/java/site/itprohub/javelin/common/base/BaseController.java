package site.itprohub.javelin.common.base;

import jakarta.servlet.http.Cookie;
import site.itprohub.javelin.dto.BaseUserInfo;
import site.itprohub.javelin.http.Pipeline.NHttpContext;
import site.itprohub.javelin.utils.StringExtensions;

public abstract class BaseController {


    public NHttpContext NHttpContext;

    public BaseUserInfo getUserInfo() {
        BaseUserInfo userInfo = this.NHttpContext.getUserInfo();
        if(userInfo == null) {
            throw new IllegalStateException("当前用户没有登录，不能获取到用户身份信息。");
        }

        return userInfo;
    }

    public boolean isAuthenticated() {
        return this.NHttpContext.isAuthenticated();
    }


    //#region httpRequest/httpResponse 方法封装
    public String getHeader(String name) {
        if (StringExtensions.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("name参数不能为空");
        }
        return this.NHttpContext.request.getHeader(name);
    }


    public void setHeader(String name, String value) {
        if (StringExtensions.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("name参数不能为空");
        }
        if (StringExtensions.isNullOrEmpty(value)) {
            throw new IllegalArgumentException("value参数不能为空");
        }

        this.NHttpContext.response.setHeader(name, value);
    }

    public String getCookie(String name) {
        if (StringExtensions.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("name参数不能为空");
        }
        return this.NHttpContext.request.cookie(name);
    }

    public void setCookie(String name, String value, int maxAge) {
        if (StringExtensions.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("name参数不能为空");
        }
        if (StringExtensions.isNullOrEmpty(value)) {
            throw new IllegalArgumentException("value参数不能为空");
        }
        this.NHttpContext.response.setCookie(name, value, maxAge);
    }

    //#endregion
}
