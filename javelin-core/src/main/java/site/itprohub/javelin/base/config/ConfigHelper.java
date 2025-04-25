package site.itprohub.javelin.base.config;

import java.io.File;
import java.nio.file.Paths;

public class ConfigHelper {

    // 判断2个目录是否相同
    // 一般情况下，假设 AppContext.BaseDirectory        :  /app/
    //            那么 Environment.CurrentDirectory    :  /app
    // 所以在比较时，要去掉结尾字符
    private static final boolean isSame = 
        Paths.get(System.getProperty("user.dir")).toString().equalsIgnoreCase(
            Paths.get(System.getProperty("user.dir")).normalize().toString());

    // 补充说明：在 .netcore 及以后版本中  AppDomain.CurrentDomain.BaseDirectory => AppContext.BaseDirectory
    //          在 .netframework 中      AppContext.BaseDirectory =>  AppDomain.CurrentDomain.BaseDirectory

    /**
     * 根据指定的相对路径，尝试获取配置文件的绝对路径。 如果尝试失败，返回NULL
     */
    public static String getFileAbsolutePath(String relativePath) {
        if (relativePath == null || relativePath.isEmpty()) {
            throw new IllegalArgumentException("relativePath cannot be null or empty");
        }

        String path = Paths.get(System.getProperty("user.dir"), relativePath).toString();
        File file = new File(path);
        if (file.exists()) {
            return path;
        }

        if (!isSame) {
            path = Paths.get(System.getProperty("user.dir"), relativePath).toString();
            file = new File(path);
            if (file.exists()) {
                return path;
            }
        }

        // 没找到约定的路径，不管了~~
        return path;
    }

    /**
     * 根据指定的相对路径，获取配置目录的绝对路径。 如果尝试失败，返回NULL
     */
    public static String getDirectoryAbsolutePath(String relativePath) {
        if (relativePath == null || relativePath.isEmpty()) {
            throw new IllegalArgumentException("relativePath cannot be null or empty");
        }

        String path = Paths.get(System.getProperty("user.dir"), relativePath).toString();
        File directory = new File(path);
        if (directory.exists() && directory.isDirectory()) {
            return path;
        }

        if (!isSame) {
            path = Paths.get(System.getProperty("user.dir"), relativePath).toString();
            directory = new File(path);
            if (directory.exists() && directory.isDirectory()) {
                return path;
            }
        }

        // 这里不检查目录是否存，由调用方检查
        return path;
    }
}