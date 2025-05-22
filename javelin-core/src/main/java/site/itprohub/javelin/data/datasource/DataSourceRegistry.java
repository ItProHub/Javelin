package site.itprohub.javelin.data.datasource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import site.itprohub.javelin.data.config.DbConfig;

public class DataSourceRegistry {
    private static final Map<String, DataSource> CACHE = new ConcurrentHashMap<>();

    public static DataSource getDataSource(String name, DbConfig dbProperties) {
        return CACHE.computeIfAbsent(name, key -> createDataSource(dbProperties));
    }

    public static DataSource getDataSource(String name, String jdbcUrl) {
        return CACHE.computeIfAbsent(name, key -> createDataSource(jdbcUrl));
    }

    private static DataSource createDataSource(DbConfig dbProperties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbProperties.getUrl());
        config.setUsername(dbProperties.getUsername());
        config.setPassword(dbProperties.getPassword());
        config.setDriverClassName(dbProperties.getDriverClassName());
        config.setMaximumPoolSize(dbProperties.getMaxPoolSize());
        config.setMinimumIdle(dbProperties.getMinIdle());
        config.setConnectionTimeout(dbProperties.getConnectionTimeout());
        
        return new HikariDataSource(config);
    }

    private static DataSource createDataSource(String jdbcUrl) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        return new HikariDataSource(config);
    }

    public static void closeAll() {
        CACHE.values().forEach(dataSource -> {
            if (dataSource instanceof HikariDataSource) {
               ((HikariDataSource)dataSource).close(); 
            } 
        });

        CACHE.clear();
    }
}
