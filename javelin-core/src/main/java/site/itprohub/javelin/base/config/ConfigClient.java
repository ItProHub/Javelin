package site.itprohub.javelin.base.config;

import site.itprohub.javelin.base.json.JsonHelper;
import site.itprohub.javelin.data.config.DbConfig;

public final class ConfigClient {

    /**
     * 默认单例引用，用于静态调用
     */
    public static final ConfigClient Instance = new ConfigClient();

    private IConfigClient client = NullValueConfigClient.Instance;



    private IConfigClient getClient() {
        return client;
    }

    /**
     * 当 EnableConfigService=false 时，指定一个替代的客户端实现
     */
    public void setClient(IConfigClient client) {
        if (client == null) {
            throw new IllegalArgumentException("client is required");
        }
        this.client = client;
    }

    void resetNull() {
        this.client = NullValueConfigClient.Instance;
    }

    // ==================== Settings Methods ====================

    /**
     * 获取一个配置参数的值
     */
    public String getSetting(String name, boolean checkExist) {
        if (isNullOrEmpty(name)) {
            throw new IllegalArgumentException("name is required");
        }

        String value = getClient().getSetting(name);
        if (checkExist && isNullOrEmpty(value)) {
            throw new RuntimeException("没有找到指定的配置参数，name: " + name);
        }

        return value;
    }

    /**
     * 获取并反序列化为对象
     */
    public <T> T getSetting(String name, boolean checkExist, Class<T> clazz) {
        String value = getSetting(name, checkExist);
        return JsonHelper.fromJson(value, clazz);
    }

    // ==================== Database Methods ====================

    public DbConfig getAppDbConfig(String connName, boolean checkExist) {
        if (isNullOrEmpty(connName)) {
            throw new IllegalArgumentException("connName is required");
        }

        DbConfig config = getClient().getAppDbConfig(connName);
        if (checkExist && config == null) {
            throw new RuntimeException("没有找到指定的数据库连接参数，connName: " + connName);
        }
        return config;
    }

    public DbConfig getTntDbConfig(String tenantId, boolean checkExist) {
        if (isNullOrEmpty(tenantId)) {
            throw new IllegalArgumentException("tenantId is required");
        }

        DbConfig config = getClient().getTntDbConfig(tenantId);
        if (checkExist && config == null) {
            throw new RuntimeException("没有找到指定的数据库连接参数, tenantId='" + tenantId);
        }
        return config;
    }

    // ==================== 工具方法 ====================

    private boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
