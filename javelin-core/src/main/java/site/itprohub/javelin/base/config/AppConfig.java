package site.itprohub.javelin.base.config;

class AppConfig {
    public final static String JAVELIN_APP_CONFIG = "javelin.app.config";

    private static String FILE_NAME = null;

    private static boolean IS_INITED = false;
    private static Object LOCK = new Object();

    static void init() {
        if (IS_INITED == false) {
            synchronized (LOCK) {
                if (IS_INITED == false) {
                    String filePath = ConfigHelper.getFileAbsolutePath(FILE_NAME);

                    System.out.println("AppConfig filePath: " + filePath);

                    initConfig(filePath);

                    IS_INITED = true;
                }
            }
        }
    }


    private static void initConfig(String filePath) {
        
    }

}
