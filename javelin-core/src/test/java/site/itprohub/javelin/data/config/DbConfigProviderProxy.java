package site.itprohub.javelin.data.config;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DbConfigProviderProxy {
    private static final Map<String, DbConfig> mockConfigMap = new HashMap<>();

    public static void setMockConfig(String name, DbConfig config) {
        mockConfigMap.put(name, config);
    }

    public static DbConfig getAppDbConfig(String dbName) throws SQLException {
        if(mockConfigMap.containsKey(dbName)) {
            return mockConfigMap.get(dbName);
        } else {
            throw new RuntimeException("数据库配置不存在：" + dbName);
         }
    }
}
