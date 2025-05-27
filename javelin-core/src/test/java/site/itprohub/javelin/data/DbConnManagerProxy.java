package site.itprohub.javelin.data;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import site.itprohub.javelin.data.config.DbConfig;

public class DbConnManagerProxy extends DbConnManager {
    private static final Map<String, DbConfig> mockConfigMap = new HashMap<>();
    private static final Map<String, DataSource> mockDataSourceMap = new HashMap<>();

    public static void setMockDbConfig(String name, DbConfig config ) {
        mockConfigMap.put(name, config);
    }

    public static void setMockDataSource(String name, DataSource dataSource) {
        mockDataSourceMap.put(name, dataSource);
    }

    public static DbConfig getAppDbConfig(String connName) {
        return mockConfigMap.get(connName);
    }

    public static DataSource getDataSource(String name, DbConfig dbConfig) {
        return mockDataSourceMap.get(name); 
    }

    public static DbContext createAppDb(String dbName) throws SQLException {
        DbConfig dbConfig = getAppDbConfig(dbName);
        DataSource dataSource = getDataSource(dbName, dbConfig);
        return new DbContext(dataSource);
    }

}
