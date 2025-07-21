package site.itprohub.javelin.base.config;

import site.itprohub.javelin.utils.StringExtensions;


public class DefaultSettingsImpl implements ISettings {

    public static final DefaultSettingsImpl Instance = new DefaultSettingsImpl();



    public String getSetting(String name, boolean checkExist) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name");
        }

        // 1. 从环境变量里面读取
        String value = System.getenv(name);

        if(StringExtensions.isNullOrEmpty(value) == false) {
            return value;
        }

        // 2. 从配置服务里面读取
        value = ConfigClient.Instance.getSetting(name, false);
        if(StringExtensions.isNullOrEmpty(value) == false) {
            return value;
        }

        // 3. 从配置文件里面读取
        value = AppConfig.getSetting(name);

        if(StringExtensions.isNullOrEmpty(value) == false) {
            return value;
        }

        return null;
    }
}
