package site.itprohub.javelin.base.config;

import site.itprohub.javelin.data.config.DbConfig;

public class NullValueConfigClient implements IConfigClient {
    public static final NullValueConfigClient Instance = new NullValueConfigClient();

    @Override
    public String getSetting(String name) {
        return null;
    }

    @Override
    public DbConfig getAppDbConfig(String name) {
        return null;
    }

    @Override
    public DbConfig getTntDbConfig(String tenantId) {
        return null;
    }

}
