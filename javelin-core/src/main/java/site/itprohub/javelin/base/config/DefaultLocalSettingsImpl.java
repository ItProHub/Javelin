package site.itprohub.javelin.base.config;

public class DefaultLocalSettingsImpl implements ILocalSettings {

    public static final DefaultLocalSettingsImpl Instance = new DefaultLocalSettingsImpl();

    public String getSetting(String name) {
        return getSetting(name, false); 
    }

    public String getSetting(String name, boolean checkExist) {
        return ""; 
    }
}
