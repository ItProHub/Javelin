package site.itprohub.javelin.base.config;


public class Settings {
    private static ISettings instance = DefaultSettingsImpl.Instance;


    public static String getSetting(String name) {
        return getSetting(name, false);
    }


    public static String getSetting(String name, boolean checkExist) {
        return instance.getSetting(name, checkExist);
    }

    public static String getSetting(String name, String defaultValue) {
        String value = getSetting(name, false);
        if(value.isEmpty()) {
            value = defaultValue;
        }
        return value;
    }
}
