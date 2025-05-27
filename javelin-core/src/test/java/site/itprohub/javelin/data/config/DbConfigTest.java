package site.itprohub.javelin.data.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

public class DbConfigTest {

    @Test
    void testDefaultPort(){
        DbConfig dbConfig = new DbConfig();
        dbConfig.dbType = "mysql";
        dbConfig.server = "localhost";
        dbConfig.dbName = "test";

        assertEquals(dbConfig.port, 3306);
    }

    @Test
    void testUrl_defaultPort() {
        DbConfig dbConfig = new DbConfig();
        dbConfig.dbType = "mysql";
        dbConfig.server = "localhost";
        dbConfig.dbName = "test";

        assertEquals(dbConfig.getUrl(), "jdbc:mysql://localhost:3306/test");
    }

    @Test
    void testUrl_customPort() {
        DbConfig dbConfig = new DbConfig();
        dbConfig.dbType = "mysql";
        dbConfig.server = "localhost";
        dbConfig.dbName = "test";
        dbConfig.port = 3307;

        assertEquals(dbConfig.getUrl(), "jdbc:mysql://localhost:3307/test");
    }

    @Test
    void testDriverClassName_default() {
        DbConfig dbConfig = new DbConfig();
        dbConfig.dbType = "mysql";

        assertEquals(dbConfig.getDriverClassName(), "com.mysql.cj.jdbc.Driver"); 
    }

    @Test
    void testDriverClassName_custom() {
        DbConfig dbConfig = new DbConfig();
        dbConfig.dbType = "sqlserver";

        assertEquals(dbConfig.getDriverClassName(), "com.microsoft.sqlserver.jdbc.SQLServerDriver");
    }

    @Test
    void testConnectionPoolSize_default() {
        DbConfig dbConfig = new DbConfig();

        assertEquals(dbConfig.getMaxPoolSize(), 10); 
        assertEquals(dbConfig.getConnectionTimeout(), 30000L);
        assertEquals(dbConfig.getMinIdle(), 5);
    }
}
