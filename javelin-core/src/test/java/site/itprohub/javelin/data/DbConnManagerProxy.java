package site.itprohub.javelin.data;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import site.itprohub.javelin.data.config.DbConfig;
import site.itprohub.javelin.data.context.ConnectionInfo;
import site.itprohub.javelin.data.context.DbContext;

public class DbConnManagerProxy extends DbConnManager {
    private static final Map<String, DbConfig> mockConfigMap = new HashMap<>();
    private static final Map<String, ConnectionInfo> mockConnectioneMap = new HashMap<>();

    public static void setMockDbConfig(String name, DbConfig config ) {
        mockConfigMap.put(name, config);
    }

    public static void setMockConnection(String name, ConnectionInfo conn) {
        mockConnectioneMap.put(name, conn);
    }

    public static DbConfig getAppDbConfig(String connName) {
        return mockConfigMap.get(connName);
    }

    public static ConnectionInfo getConnection(String name, DbConfig dbConfig) {
        return mockConnectioneMap.get(name); 
    }

    public static DbContext createAppDb(String dbName) throws SQLException {
        DbConfig dbConfig = getAppDbConfig(dbName);
        ConnectionInfo conn = new ConnectionInfo(dbConfig);
        return new DbContext(conn);
    }

}
