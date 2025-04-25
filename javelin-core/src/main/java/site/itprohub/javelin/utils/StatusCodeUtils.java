package site.itprohub.javelin.utils;

public class StatusCodeUtils {
    public static boolean isServerError(int statusCode) {
        return statusCode >= 500 && statusCode < 600;
    }
}
