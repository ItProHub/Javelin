package site.itprohub.javelin.base.config;

import site.itprohub.javelin.base.config.models.AppConfiguration;

class AppConfig {
    public final static String JAVELIN_APP_CONFIG = "javelin.app.config";

    private static String FILE_NAME = null;

    private static boolean IS_INITED = false;
    private static Object LOCK = new Object();

    private static AppConfigObject s_configuration;

    static void init() {
        if (IS_INITED == false) {
            synchronized (LOCK) {
                if (IS_INITED == false) {
                    String filePath = ConfigHelper.getFileAbsolutePath(JAVELIN_APP_CONFIG);

                    System.out.println("AppConfig filePath: " + filePath);

                    initConfig(filePath);

                    IS_INITED = true;
                }
            }
        }
    }


    private static void initConfig(String filePath) {
        AppConfiguration config = AppConfiguration.loadFromFile(filePath, false);
        if (config == null) {
            config = new AppConfiguration();
        }
        s_configuration = new AppConfigObject(config);
        IS_INITED = true;

    }

    public static String getSetting(String name) {
        if(IS_INITED == false) {
            init();
        }

        return s_configuration.getSetting(name);

    }

}
