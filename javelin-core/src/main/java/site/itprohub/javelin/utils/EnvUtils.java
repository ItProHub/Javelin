package site.itprohub.javelin.utils;

import java.util.Date;

public class EnvUtils {
    public static String ApplicationName;

    public static Date ApplicationStartTime;

    static {
        ApplicationStartTime = new Date();
    }

    public static void init(Class<?> appClass) {
        String envApplicationName = System.getenv("ApplicationName");
        ApplicationName = envApplicationName != null ? envApplicationName : appClass.getPackage().getName();
    }
}
