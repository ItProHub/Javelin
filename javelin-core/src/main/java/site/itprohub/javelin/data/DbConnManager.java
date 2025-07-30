package site.itprohub.javelin.data;

import java.sql.SQLException;

import site.itprohub.javelin.data.config.DbConfig;
import site.itprohub.javelin.data.config.DbConfigProvider;
import site.itprohub.javelin.data.context.ConnectionInfo;
import site.itprohub.javelin.data.context.DbContext;

public class DbConnManager {

    public static DbConfig getAppDbConfig(String connName) throws SQLException {
        if ( connName == null || connName.isEmpty()) {
            throw new IllegalArgumentException("Connection name cannot be null or empty");
        }

        return DbConfigProvider.getAppDbConfig(connName);
    }


    public static DbContext createTenantDb(String tenantId) {
        return new DbContext(null);
    }

    public static DbContext createAppDb(String dbName) throws SQLException {

        DbConfig dbConfig = getAppDbConfig(dbName);

        // DataSource dataSource = DataSourceRegistry.getDataSource(dbName, dbConfig);

        // Connection conn = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword());

        ConnectionInfo connInfo = new ConnectionInfo(dbConfig);

        return new DbContext(connInfo);
    }

}
