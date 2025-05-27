
package site.itprohub.javelin.data;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DbContextTest {

    @Test
    void testGetConnection_shouldReturnValidConnection() throws SQLException {
        // 模拟 DataSource 和 Connection
        javax.sql.DataSource mockDataSource = mock(javax.sql.DataSource.class);
        Connection mockConnection = mock(Connection.class);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);

        // 构造 DbContext
        DbContext dbContext = new DbContext(mockDataSource);
        // 手动打开连接
        dbContext.openConnection();
        // 调用 getConnection
        Connection conn = dbContext.getConnection();
        assertNotNull(conn);
        assertEquals(mockConnection, conn);
    }

    @Test
    void testClose_shouldCloseConnection() throws SQLException {
        // 模拟 Connection
        Connection mockConnection = mock(Connection.class);
        javax.sql.DataSource mockDataSource = mock(javax.sql.DataSource.class);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);

        // 使用 DbContext 并关闭
        DbContext dbContext = new DbContext(mockDataSource);
        dbContext.openConnection(); // 打开连接
        dbContext.close();         // 关闭连接

        verify(mockConnection, times(1)).close();
    }

    @Test
    void testGetConnection_lazyInit() throws SQLException {
        // 模拟连接仅在第一次调用 getConnection 时才获取
        javax.sql.DataSource mockDataSource = mock(javax.sql.DataSource.class);
        Connection mockConnection = mock(Connection.class);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);

        DbContext dbContext = new DbContext(mockDataSource);
        // 未调用 getConnection 前，不应创建连接
        verify(mockDataSource, times(0)).getConnection();

        // 调用后才初始化
        dbContext.openConnection();
        verify(mockDataSource, times(1)).getConnection();
    }
}
