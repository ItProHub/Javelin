package site.itprohub.javelin.startup;

import site.itprohub.javelin.JavelinCoreInit;
import site.itprohub.javelin.base.config.ConfigClient;
import site.itprohub.javelin.base.init.ApplicationInitializer;
import site.itprohub.javelin.clients.serviceClients.MoonClient;
import site.itprohub.javelin.web.security.Auth.AuthOptions;

public class JavelinInitializer {


    public static void frameworkInit(){
        System.out.println("----------------- Framework Initializer  ---------------------------------------");

        // 初始化数据访问层
        initDAL();

        // 初始化配置服务客户端
        setConfigClient();
    }

    public static void applicationInit(Class<?> appClass){
        AuthOptions.init();
        System.out.println("----------------- Application Initializer  ---------------------------------------");
        ApplicationInitializer.execute(appClass);

        System.out.println("----------------- Start BackgroundTasks  -----------------------------------------");
        // 启动后台任务

        System.out.println("----------------------------------------------------------------------------------");
    }

    private static void setConfigClient() {
        ConfigClient.Instance.setClient(MoonClient.Instance);
    }

    private static void initDAL() {
        System.out.println("----------------- Init DAL  -----------------------------------------");
        JavelinCoreInit.initDAL();
    }
}
