package site.itprohub.javelin.base.config;


public class LocalSettings{
    private static ILocalSettings s_instance = DefaultLocalSettingsImpl.Instance;

    public static String getSetting(String name) {
        return s_instance.getSetting(name);
    }


    public static String getSetting(String name, boolean checkExist) {
        return s_instance.getSetting(name, checkExist);
    }

    public static String getSetting(String name, String defaultValue) {
        String value = getSetting(name);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        return value;
    }

    public static int getInt(String name, int defaultValue) {
        String value = getSetting(name);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }
    
}