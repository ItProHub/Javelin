package site.itprohub.javelin.data;

import java.sql.SQLException;

import site.itprohub.javelin.data.config.DbConfigProvider;

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

        return dbConfig.creaDbContext();
    }

}
