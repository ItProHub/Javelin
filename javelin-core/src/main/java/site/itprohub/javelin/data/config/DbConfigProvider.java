package site.itprohub.javelin.data.config;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import site.itprohub.javelin.data.context.DbContext;

public class DbConfigProvider {

    private static final Map<String, DbConfig> configMap = new HashMap<>();

    public static DbConfig getAppDbConfig(String dbName) throws SQLException {
        if ( dbName == null || dbName.isEmpty()) {
            throw new IllegalArgumentException("dbName name cannot be null or empty");
        }

        DbConfig config = configMap.get(dbName);
        if (config == null) {
            try ( DbContext db = DbConfigUtils.CreateConfigDbContext() ) {
                String sql = "select * from dbconfig where name = ?";
                config = db.CPQuery().create(sql, dbName).toSingle(DbConfig.class);
                if (config == null) {
                    throw new RuntimeException("数据库配置不存在：" + dbName);    
                }
                configMap.put(dbName, config);
            } catch (SQLException e) {
                throw new RuntimeException("数据库配置不存在：" + dbName, e); 
            }
        }

        return config;

    }
}
