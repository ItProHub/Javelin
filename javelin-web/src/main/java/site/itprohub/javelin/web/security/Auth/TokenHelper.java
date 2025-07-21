package site.itprohub.javelin.web.security.Auth;

import jakarta.servlet.http.Cookie;
import site.itprohub.javelin.http.Pipeline.NHttpContext;
import site.itprohub.javelin.utils.StringExtensions;

public class TokenHelper {
    public static void loadToken(NHttpContext httpContext) {
        if ( httpContext.user != null ) {
            return;
        }

        String token = httpContext.request.getHeader(AuthOptions.headerName);

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
        Cookie cookie = new Cookie(AuthOptions.cookieName, token);
        cookie.setMaxAge(expirationSeconds);
        httpContext.response.addCookie(cookie);
    }


}
