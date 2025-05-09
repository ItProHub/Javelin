package site.itprohub.javelin.data.config;

import java.sql.SQLException;

import site.itprohub.javelin.data.DbContext;

public class DbConfigUtils {
    public static DbContext CreateConfigDbContext() throws SQLException {
        return DbContext.create(EnvConfig.getDbUrl());
    }
}
