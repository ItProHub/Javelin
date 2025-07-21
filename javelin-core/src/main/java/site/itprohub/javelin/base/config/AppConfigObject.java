package site.itprohub.javelin.base.config;

import site.itprohub.javelin.base.config.models.*;
import site.itprohub.javelin.data.config.DbConfig;

import java.util.*;

public class AppConfigObject {

    private final AppConfiguration config;

    public AppConfiguration getConfiguration() {
        return config;
    }

    private final Map<String, String> settings;
    private final Map<String, ConnectionStringSetting> conns;
    private final Map<String, DbConfig> dbConfigs;

    public AppConfigObject(AppConfiguration config) {
        if (config == null) {
            throw new IllegalArgumentException("config is required");
        }

        config.correctData();
        this.config = config;

        settings = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (AppSetting x : config.appSettings) {
            if (hasText(x.key)) {
                settings.put(x.key, Optional.ofNullable(x.value).orElse(""));

                // 兼容 KEY
                String name2 = getConfName(x.key);
                if (!x.key.equals(name2)) {
                    settings.put(name2, Optional.ofNullable(x.value).orElse(""));
                }
            }
        }

        conns = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (ConnectionStringSetting x : config.connectionStrings) {
            if (hasText(x.name) && hasText(x.connectionString)) {
                conns.put(x.name, x);

                String name2 = getConfName(x.name);
                if (!x.name.equals(name2)) {
                    conns.put(name2, x);
                }
            }
        }


        dbConfigs = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (XmlDbConfig x : config.dbConfigs) {
            if (hasText(x.name) && hasText(x.server)) {
                DbConfig dbConfig = x.toDbConfig();
                dbConfigs.put(x.name, dbConfig);

                String name2 = getConfName(x.name);
                if (!x.name.equals(name2)) {
                    dbConfigs.put(name2, dbConfig);
                }
            }
        }


    }

    public String getSetting(String name) {
        if (!hasText(name)) {
            throw new IllegalArgumentException("name is required");
        }
        return settings.get(name);
    }

    public ConnectionStringSetting getConnectionString(String name) {
        if (!hasText(name)) {
            throw new IllegalArgumentException("name is required");
        }
        ConnectionStringSetting setting = conns.get(name);
        return setting != null ? setting.clone() : null;
    }

    public DbConfig getDbConfig(String name) {
        if (!hasText(name)) {
            throw new IllegalArgumentException("name is required");
        }
        DbConfig config = dbConfigs.get(name);
        return config != null ? config.clone() : null;
    }

    // 工具方法
    private boolean hasText(String text) {
        return text != null && !text.trim().isEmpty();
    }

    // 模拟 GetConfName：把点号替换成下划线
    private String getConfName(String name) {
        return name.replace('.', '_');
    }
}
