package site.itprohub.javelin.data.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import site.itprohub.javelin.data.DbContext;
import site.itprohub.javelin.data.datasource.DataSourceRegistry;

public class DbConfigUtils {
    public static DbContext CreateConfigDbContext() throws SQLException {
        DataSource dataSource = DataSourceRegistry.getDataSource("config", EnvConfig.getDbUrl());
        return DbContext.create(dataSource);
    }
}
