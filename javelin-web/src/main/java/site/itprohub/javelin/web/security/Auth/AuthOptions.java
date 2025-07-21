package site.itprohub.javelin.web.security.Auth;

import site.itprohub.javelin.base.config.LocalSettings;

public final class AuthOptions {

    // 登录凭证在请求头中的名称，默认值："x-token"
    public static String headerName;

    // 登录凭证在 Cookie 中的名称，默认值："xtoken"
    public static String cookieName;

    // 是否启用 JWT 自动续期机制
    public static boolean jwtTokenExpirationRenewal;

    // 静态初始化
    static {
        headerName = LocalSettings.getSetting("Javelin_Authentication_HeaderName", "x-token");
        cookieName = LocalSettings.getSetting("Javelin_Authentication_CookieName", "xtoken");
        jwtTokenExpirationRenewal = LocalSettings.getBool("Javelin_JwtToken_ExpirationRenewal", true);
    }

    /**
     * 主动触发类初始化（等价于 C# 的 Init 方法）
     */
    public static void init() {
        // no-op
    }

    // 禁止实例化
    private AuthOptions() {}
}

