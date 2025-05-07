package site.itprohub.javelin.startup;

import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;

import site.itprohub.javelin.http.tomcat.JavelinEmbeddedTomcatServer;
import site.itprohub.javelin.utils.EnvUtils;

public class JavelinStarter {

    private Set<BaseAppStarter> starters = new HashSet<>();

    static {
        // 静态初始化块，在类加载时执行
    }

    public void run(Class<?> appClass, String[] args, AppStartupOption option) throws Exception
    {
        EnvUtils.init(appClass); // 自动设置应用名
        System.out.println("Javelin starting from package: " + EnvUtils.ApplicationName); // 打印应用名

        javelinInit();

        JavelinEmbeddedTomcatServer server = new JavelinEmbeddedTomcatServer(); // 创建Tomcat服务器实例

        JavelinHost.initNHttpApplication();

        applicationInit();

        System.out.println(" Javelin initialized!");

        server.start();

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


    private void loadStarters() {
        Reflections reflections = new Reflections(EnvUtils.ApplicationName);
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
