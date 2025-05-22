package site.itprohub.javelin.data;

import java.sql.SQLException;

import javax.sql.DataSource;

import site.itprohub.javelin.data.config.DbConfig;
import site.itprohub.javelin.data.config.DbConfigProvider;
import site.itprohub.javelin.data.datasource.DataSourceRegistry;

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

        DataSource dataSource = DataSourceRegistry.getDataSource(dbName, dbConfig);

        return new DbContext(dataSource);
    }

}
