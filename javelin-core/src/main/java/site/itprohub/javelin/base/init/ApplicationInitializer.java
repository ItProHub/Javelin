package site.itprohub.javelin.base.init;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

public class ApplicationInitializer {
    public static void execute(Class<?> appClass) {
        executeAllAppInitializers();
    }

    public static void executeAllAppInitializers() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> resources = classLoader.getResources("");
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                String rootPath = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
                File root = new File(rootPath);
                if (root.exists()) {
                    scanDirectoryForAppInitializer(root, root.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void scanDirectoryForAppInitializer(File dir, String rootPath) {
        if (!dir.isDirectory()) return;

        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectoryForAppInitializer(file, rootPath);
            } else if (file.getName().equals("AppInitializer.class")) {
                String className = toClassName(file, rootPath);
                try {
                    Class<?> clazz = Class.forName(className);
                    Method method = clazz.getDeclaredMethod("init");
                    if (Modifier.isStatic(method.getModifiers())) {
                        System.out.println("Execute " + className + ".init()");
                        method.setAccessible(true);
                        method.invoke(null);
                    }
                } catch (ClassNotFoundException e) {
                    System.out.println("无法加载类：" + className);
                } catch (NoSuchMethodException e) {
                    System.out.println("类 " + className + " 没有 init() 方法");
                } catch (Exception e) {
                    System.out.println("执行 " + className + ".init() 出错：");
                    e.printStackTrace();
                }
            }
        }
    }

    private static String toClassName(File classFile, String rootPath) {
        String absPath = classFile.getAbsolutePath();
        String relativePath = absPath.substring(rootPath.length() + 1)
                .replace(File.separatorChar, '.');
        return relativePath.replaceAll("\\.class$", "");
    }


}
