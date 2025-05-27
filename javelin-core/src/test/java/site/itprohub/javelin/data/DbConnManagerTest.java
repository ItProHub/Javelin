package site.itprohub.javelin.data;

import java.sql.Connection;
import javax.sql.DataSource;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import site.itprohub.javelin.data.config.DbConfig;

public class DbConnManagerTest {

    @Test
    void testGetAppDbConfig_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> DbConnManager.getAppDbConfig(null));
        assertThrows(IllegalArgumentException.class, () -> DbConnManager.getAppDbConfig(""));
    }

    @Test
    void testGetAppDbConfig_returnsMockConfig() throws Exception {
        DbConfig mockConfig = mock(DbConfig.class);
        DbConnManagerProxy.setMockDbConfig("test", mockConfig);

        DbConfig result = DbConnManagerProxy.getAppDbConfig("test");

        assertNotNull(result);
        assertEquals(mockConfig, result);
    }

    @Test
    void testCreateAppDb() throws Exception {
        DbConfig mockConfig = mock(DbConfig.class);
        Connection mockConnection = mock(Connection.class);
        DataSource mockDataSource = mock(DataSource.class);

        when(mockDataSource.getConnection()).thenReturn(mockConnection);

        DbConnManagerProxy.setMockDbConfig("test", mockConfig);
        DbConnManagerProxy.setMockDataSource("test", mockDataSource);


        DbContext dbContext = new DbContext(mockDataSource);
        dbContext.openConnection();
        assertNotNull(dbContext);
        assertEquals(mockConnection, dbContext.getConnection());
    }

    @Test
    void testCreateTenantDb() {
        DbContext dbContext = new DbContext(null);
        assertNotNull(dbContext);
    }
}
