package site.itprohub.javelin.base.config;

import site.itprohub.javelin.data.config.DbConfig;

public interface IConfigClient {
    String getSetting(String name);

    DbConfig getAppDbConfig(String name);

    DbConfig getTntDbConfig(String tenantId);

    
}
