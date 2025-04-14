package site.itprohub.javelin.startup;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Set;
import site.itprohub.javelin.core.ClassScanner;
import site.itprohub.javelin.rest.Router;
import com.sun.net.httpserver.HttpServer;

import site.itprohub.javelin.context.JavelinContext;


/**
 * 应用启动类
 */

public class AppStartup {
    public static void run(Class<?> appClass, String[] args)
    {
        String basePackage = appClass.getPackage().getName(); // 自动获取包名
        System.out.println("🚀 Javelin starting from package: " + basePackage);

        Set<Class<?>> controllers = ClassScanner.scan(basePackage);

        int port = resolvePort(); // ✅ 获取端口号
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0); // 默认端口8080     
        } catch (IOException e) {
            throw new RuntimeException("Failed to start HTTP server", e);
        }
           

        JavelinContext context = new JavelinContext();
        new Router().registerRoutes(server, controllers, context);

        server.start();

        System.out.println("🌐 HTTP Server started at http://localhost:" + port);

        System.out.println("✅ Javelin initialized!");
    }

    private static int resolvePort() { // ✅ 解析端口号
        String portStr = System.getenv("JAVELIN_PORT"); // 从环境变量中获取端口号
        if ( portStr == null || portStr.isEmpty() ) {
            portStr = System.getProperty("javelin.port", "8080"); // 从系统属性中获取端口号
        }

        return Integer.parseInt(portStr);
    }
    
}
