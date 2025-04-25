package site.itprohub.javelin.startup;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;

import com.sun.net.httpserver.HttpServer;

import site.itprohub.javelin.annotations.RestController;
import site.itprohub.javelin.base.config.LocalSettings;
import site.itprohub.javelin.common.Const;
import site.itprohub.javelin.context.JavelinContext;
import site.itprohub.javelin.rest.Router;

public class JavelinStarter {

    private static final Router ROUTER;
    private static final JavelinContext CONTEXT;

    public static String BASE_PACKAGE;

    private Set<BaseAppStarter> starters = new HashSet<>();

    static { // 静态初始化块，在类加载时执行
        ROUTER = new Router(); // 创建Router实例
        CONTEXT = new JavelinContext(); // 创建JavelinContext实例        
    }

    public void run(Class<?> appClass, String[] args, AppStartupOption option) throws Exception
    {
        BASE_PACKAGE = appClass.getPackage().getName(); // 自动获取包名
        System.out.println("🚀 Javelin starting from package: " + BASE_PACKAGE);

        javelinInit();

        HttpServer server = createServer();
        configRouter(server);

        JavelinHost.initNHttpApplication();

        applicationInit();

        server.start();

        System.out.println("✅ Javelin initialized!");
    }

    private void javelinInit()
    {
        loadStarters();

        for (BaseAppStarter starter : starters) {
            try {
                starter.preJavelinInit(); // 调用preInit方法
            } catch (Exception e) {
                e.printStackTrace(); // 打印异常堆栈信息     
            }
        }

        JavelinInitializer.frameworkInit();

        for (BaseAppStarter starter : starters) {
            try {
                starter.postJavelinInit(); // 调用preInit方法
            } catch (Exception e) {
                e.printStackTrace(); // 打印异常堆栈信息     
            }
        }
    }

    private void applicationInit()
    {
        for (BaseAppStarter starter : starters) {
            try {
                starter.preApplicationInit(); // 调用preInit方法
            } catch (Exception e) {
                e.printStackTrace(); // 打印异常堆栈信息     
            }
        }

        JavelinInitializer.applicationInit();


        for (BaseAppStarter starter : starters) {
            try {
                starter.postApplicationInit(); // 调用preInit方法
            } catch (Exception e) {
                e.printStackTrace(); // 打印异常堆栈信息     
            }
        }
    }


    private static HttpServer createServer() throws IOException { // ✅ 创建HttpServer实例
        // 获取端口号
        int port = LocalSettings.getInt(Const.Names.JAVELIN_PORT, 8080);
        System.out.println("🌐 HTTP Server started at http://localhost:" + port);

        return HttpServer.create(new InetSocketAddress(port), 0);
    }

    // ✅ 配置路由
    private void configRouter(HttpServer server) {
        Reflections reflections = new Reflections(BASE_PACKAGE);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(RestController.class);
        ROUTER.registerRoutes(server, controllers, CONTEXT); // 注册路由
    }

    private void loadStarters() {
        Reflections reflections = new Reflections(BASE_PACKAGE);
        Set<Class<? extends BaseAppStarter>> starterClasses = reflections.getSubTypesOf(BaseAppStarter.class);

        for (Class<? extends BaseAppStarter> starterClass : starterClasses) {
            try {
                starters.add(starterClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}
