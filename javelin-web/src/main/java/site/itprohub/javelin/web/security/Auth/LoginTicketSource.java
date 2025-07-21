package site.itprohub.javelin.web.security.Auth;

public enum LoginTicketSource {
    /**
     * 从请求头中取得
     */
    HEADER(100),

    /**
     * 从 Cookie 中取得
     */
    COOKIE(200),

    /**
     * 来自于服务端会话
     */
    SESSION(900),

    /**
     * 其它来源
     */
    OTHERS(999);

    private final int code;

    LoginTicketSource(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static LoginTicketSource fromCode(int code) {
        for (LoginTicketSource source : values()) {
            if (source.code == code) {
                return source;
            }
        }
        return OTHERS;
    }
}

