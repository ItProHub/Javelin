package site.itprohub.javelin.data.config;

import java.sql.SQLException;


import site.itprohub.javelin.data.context.ConnectionInfo;
import site.itprohub.javelin.data.context.DbContext;

public class DbConfigUtils {
    public static DbContext CreateConfigDbContext() throws SQLException {
        //DataSource dataSource = DataSourceRegistry.getDataSource("config", EnvConfig.getDbUrl());
        ConnectionInfo conn = new ConnectionInfo(EnvConfig.getDbUrl());

        return DbContext.create(conn);
    }
}
