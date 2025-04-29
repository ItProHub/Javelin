package site.itprohub.javelin.startup;


/**
 * 应用启动类
 */

public class AppStartup {

    static  AppStartupOption STARTUP_OPTION;

    public static void run(Class<?> appClass, String[] args) { // 重载run方法，支持AppStartupOption参数
        run(appClass, args, null); // 调用重载方法，传入null作为默认值
    }

    public static void run(Class<?> appClass, String[] args, AppStartupOption startupOption) {

        try{
            startupOption = initOption(appClass, startupOption);

            JavelinStarter starter = new JavelinStarter();

            // 调用JavelinStarter的run方法，传入AppStartupOption参数
            starter.run(appClass, args, startupOption);
        } catch (Exception e) { 
            // 捕获异常
            System.err.println("Javelin startup failed: " + e.getMessage()); // 打印异常信息
        }
    }

    /**
     * 初始化AppStartupOption参数
     * @param appClass 应用类
     * @param startupOption 启动选项
     * @return AppStartupOption实例
     * @throws Exception 异常
     */
    private static AppStartupOption initOption(Class<?> appClass, AppStartupOption startupOption) throws Exception { // ✅ 初始化AppStartupOption参数
        if(appClass == null) { // 如果appClass为空，则抛出异常
            throw new IllegalArgumentException("appClass cannot be null"); // 抛出异常
        }

        AppStartupOption option = AppStartupOption.create(startupOption);

        // 给静态变量赋值
        AppStartup.STARTUP_OPTION = option;

        return option;
    }

    
}
