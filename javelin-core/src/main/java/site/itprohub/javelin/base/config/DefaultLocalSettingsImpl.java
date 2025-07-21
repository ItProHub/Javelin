package site.itprohub.javelin.base.config;

import site.itprohub.javelin.utils.StringExtensions;

public class DefaultLocalSettingsImpl implements ILocalSettings {

    public static final DefaultLocalSettingsImpl Instance = new DefaultLocalSettingsImpl();

    @Override
    public String getSetting(String name) {
        return getSetting(name, false); 
    }

    @Override
    public String getSetting(String name, boolean checkExist) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name");
        }

        String value = System.getenv(name);

        if ( StringExtensions.isNullOrEmpty(value) == false) {
            return value;
        }

        value = AppConfig.getSetting(name);

        if( StringExtensions.isNullOrEmpty(value) == false) {
            return value;
        }

        if (checkExist) {
            throw new IllegalArgumentException(String.format("Setting '%s' is not found", name));
        } else {
            return null;
        }

    }
}
