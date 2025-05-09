package site.itprohub.javelin.data.config;

import java.sql.SQLException;

import site.itprohub.javelin.data.DbConfig;
import site.itprohub.javelin.data.DbContext;

public class DbConfigProvider {
    public static DbConfig getAppDbConfig(String dbName) throws SQLException {
        
        try ( DbContext db = DbConfigUtils.CreateConfigDbContext() ) {
            String sql = "select * from dbconfig where name = ?";
            return db.CPQuery().create(sql, dbName).toSingle(DbConfig.class);
        }

    }
}
