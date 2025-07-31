package site.itprohub.javelin.web.security.Auth;

import site.itprohub.javelin.http.Pipeline.NHttpContext;
import site.itprohub.javelin.utils.StringExtensions;

public class TokenHelper {
    public static void loadToken(NHttpContext httpContext) {
        if ( httpContext.user != null ) {
            return;
        }

        String token = httpContext.request.getHeader(AuthOptions.headerName);

        // 尝试从cookie里面获取token
        if (StringExtensions.isNullOrEmpty(token)) {
            token = httpContext.request.cookie(AuthOptions.cookieName);
        }

        if ( StringExtensions.isNullOrEmpty(token) == false ) {
            setContextUser(httpContext, token, LoginTicketSource.HEADER);
        }
    }


    private static void setContextUser(NHttpContext httpContext, String token, LoginTicketSource source) {
        LoginTicket ticket = AuthenticationManager.decodeToken(token);

        if ( ticket == null || ticket.getUser() == null ) {
            return;
        }

        httpContext.user = new NPrincipal(ticket, source, token);

    }

    public static void writeCookie(String token, int expirationSeconds, NHttpContext httpContext) {
        httpContext.response.setCookie(AuthOptions.cookieName, token, expirationSeconds);
    }


}
