package site.itprohub.javelin.clients.serviceClients;

import site.itprohub.javelin.base.config.LocalSettings;

public class MoonClientOptions {
    public static final int settingsCacheSeconds = LocalSettings.getInt("Javelin_ConfigClient_SettingsCacheSeconds", 30 * 60);
}
