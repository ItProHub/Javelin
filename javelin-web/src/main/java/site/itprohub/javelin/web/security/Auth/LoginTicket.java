package site.itprohub.javelin.web.security.Auth;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import site.itprohub.javelin.dto.BaseUserInfo;

public final class LoginTicket {

    private BaseUserInfo user;
    private String issuer;
    private long iat;
    private long exp;

    // --- getters/setters ---
    public BaseUserInfo getUser() {
        return user;
    }

    public void setUser(BaseUserInfo user) {
        this.user = user;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public long getIat() {
        return iat;
    }

    public void setIat(long iat) {
        this.iat = iat;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    /**
     * 验证是否过期
     */
    boolean verifyExpiration() {
        long nowTicks = System.currentTimeMillis() * 10_000 + 621355968000000000L;  // Java模拟C#的Ticks
        long nowNumber = Long.parseLong(toNumber(LocalDateTime.now()));

        if (this.exp > 99991231235959L) {
            return this.exp >= nowTicks;
        } else {
            return this.exp >= nowNumber;
        }
    }

    /**
     * 是否需要自动续期（过了一半有效期）
     */
    boolean isNeedRefresh() {
        boolean isLongTime = this.exp > 99991231235959L;

        LocalDateTime issue = isLongTime ? ticksToDateTime(this.iat) : numberToDateTime(this.iat);
        LocalDateTime expire = isLongTime ? ticksToDateTime(this.exp) : numberToDateTime(this.exp);

        int totalSeconds = (int) java.time.Duration.between(issue, expire).getSeconds();
        LocalDateTime half = issue.plusSeconds(totalSeconds / 2);

        return LocalDateTime.now().isAfter(half);
    }

    /**
     * 获取有效期秒数
     */
    int getSeconds() {
        boolean isLongTime = this.exp > 99991231235959L;
        LocalDateTime start = isLongTime ? ticksToDateTime(this.iat) : numberToDateTime(this.iat);
        LocalDateTime end = isLongTime ? ticksToDateTime(this.exp) : numberToDateTime(this.exp);
        return (int) java.time.Duration.between(start, end).getSeconds();
    }

    // --- 工具方法 ---

    /**
     * 将 C# 的 ticks（long）转换为 LocalDateTime
     */
    private static LocalDateTime ticksToDateTime(long ticks) {
        long millisSinceEpoch = (ticks - 621355968000000000L) / 10_000;
        return Instant.ofEpochMilli(millisSinceEpoch).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 将 ToNumber 格式（yyyyMMddHHmmss）转换为 LocalDateTime
     */
    private static LocalDateTime numberToDateTime(long num) {
        String str = String.valueOf(num);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.parse(str, formatter);
    }

    /**
     * 将当前时间转换为 ToNumber 格式（yyyyMMddHHmmss）
     */
    private static String toNumber(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}

