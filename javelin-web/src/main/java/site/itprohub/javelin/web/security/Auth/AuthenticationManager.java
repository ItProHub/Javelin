package site.itprohub.javelin.web.security.Auth;

import jakarta.servlet.http.Cookie;
import site.itprohub.javelin.base.config.Settings;
import site.itprohub.javelin.dto.BaseUserInfo;
import site.itprohub.javelin.http.Pipeline.HttpPipelineContext;
import site.itprohub.javelin.http.Pipeline.NHttpContext;
import site.itprohub.javelin.utils.EnvUtils;
import site.itprohub.javelin.utils.MapUtil;

/***
 * 用户身份验证操作相关的工具类
 */
public class AuthenticationManager
{
    private static String s_SecretKey = Settings.getSetting("Javelin_Authentication_SecretKey", true);

    public static String login(BaseUserInfo userInfo, int expirationSeconds)
    {
        String token = getLoginToken(userInfo, expirationSeconds);

        HttpPipelineContext ctx = HttpPipelineContext.current();

        TokenHelper.writeCookie(token, expirationSeconds, ctx.httpContext);

        ctx.httpContext.setUserInfoToLog(userInfo);

        return token;
    }


    public static String getLoginToken(BaseUserInfo userInfo, int expirationSeconds) {
        if (userInfo == null) {
            throw new IllegalArgumentException("userInfo is required");
        }

        userInfo.validate();

        if(expirationSeconds < 0) 
            throw new IllegalArgumentException("expirationSeconds");

        LoginTicket ticket = new LoginTicket();
        ticket.setUser(userInfo);
        ticket.setIssuer(EnvUtils.ApplicationName);

        return JwtHelper.generateToken(s_SecretKey, MapUtil.toMap(ticket), expirationSeconds * 1000);
    }


    public static LoginTicket decodeToken(String token) {
        return JwtHelper.decodeToken(s_SecretKey, token);
    }



    public static void authenticationUser(NHttpContext httpContext) {
        if( httpContext == null ) {
            throw new IllegalArgumentException("httpContext");
        }

        TokenHelper.loadToken(httpContext);
    }


    public static void logout()
    {
        HttpPipelineContext ctx = HttpPipelineContext.current();

        NHttpContext httpContext = ctx.httpContext;
        httpContext.response.setCookie(AuthOptions.cookieName, "", 0);
    }


}