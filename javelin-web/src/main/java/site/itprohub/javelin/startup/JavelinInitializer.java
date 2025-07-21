package site.itprohub.javelin.startup;

import site.itprohub.javelin.base.config.ConfigClient;
import site.itprohub.javelin.clients.serviceClients.MoonClient;

public class JavelinInitializer {


    public static void frameworkInit(){
        System.out.println("----------------- Framework Initializer  ---------------------------------------");

        // 初始化数据访问层

        // 初始化配置服务客户端
        setConfigClient();
    }

    public static void applicationInit(){
        System.out.println("----------------- Application Initializer  ---------------------------------------");


        System.out.println("----------------- Start BackgroundTasks  -----------------------------------------");
        // 启动后台任务

        System.out.println("----------------------------------------------------------------------------------");
    }

    private static void setConfigClient() {
        ConfigClient.Instance.setClient(MoonClient.Instance);
    }
}
