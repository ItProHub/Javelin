
package site.itprohub.javelin.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import site.itprohub.javelin.data.config.DbConfig;
import site.itprohub.javelin.data.context.ConnectionInfo;
import site.itprohub.javelin.data.context.DbContext;
import site.itprohub.javelin.data.multidb.DatabaseClients;
import site.itprohub.javelin.data.multidb.DbClientFactory;
import site.itprohub.javelin.data.multidb.MysqlClientProvider;
import site.itprohub.javelin.data.multidb.SqlServerClientProvider;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DbContextTest {

     @BeforeEach
    void setup() {
        DbClientFactory.registerProvider(DatabaseClients.MYSQL, MysqlClientProvider.INSTANCE);
        DbClientFactory.registerProvider(DatabaseClients.SQLSERVER, SqlServerClientProvider.INSTANCE);
    }
    @Test
    void testGetConnection_shouldReturnValidConnection() throws SQLException {
        DbConfig dbConfig = DbConnManager.getAppDbConfig("test");

        ConnectionInfo connInfo = new ConnectionInfo(dbConfig);

        // 构造 DbContext
        DbContext dbContext = new DbContext(connInfo);
        // 手动打开连接
        dbContext.openConnection();
        // 调用 getConnection
        Connection conn = dbContext.getConnection();
        assertNotNull(conn);
    }

    @Test
    void testClose_shouldCloseConnection() throws SQLException {
        DbConfig dbConfig = DbConnManager.getAppDbConfig("test");

        ConnectionInfo connInfo = new ConnectionInfo(dbConfig);
        // 使用 DbContext 并关闭
        DbContext dbContext = new DbContext(connInfo);
        dbContext.openConnection(); // 打开连接
        dbContext.close();         // 关闭连接


        RuntimeException ex = assertThrows(
            RuntimeException.class,
            () -> dbContext.getConnection()
        );
    }

    @Test
    void testGetConnection_lazyInit() throws SQLException {
        // 模拟连接仅在第一次调用 getConnection 时才获取
        javax.sql.DataSource mockDataSource = mock(javax.sql.DataSource.class);
        Connection mockConnection = mock(Connection.class);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);

        DbConfig dbConfig = DbConnManager.getAppDbConfig("test");


        DbContext dbContext = DbContext.create(dbConfig);
        // 未调用 openConnection 前，不应创建连接
         RuntimeException ex = assertThrows(
            RuntimeException.class,
            () -> dbContext.getConnection()
        );

        // 调用后才初始化
        dbContext.openConnection();
        assertNotNull(dbContext.getConnection());
    }
}
