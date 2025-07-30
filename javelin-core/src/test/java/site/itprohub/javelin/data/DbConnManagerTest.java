package site.itprohub.javelin.data;

import java.sql.Connection;
import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import site.itprohub.javelin.data.config.DbConfig;
import site.itprohub.javelin.data.context.ConnectionInfo;
import site.itprohub.javelin.data.context.DbContext;
import site.itprohub.javelin.data.multidb.DatabaseClients;
import site.itprohub.javelin.data.multidb.DbClientFactory;
import site.itprohub.javelin.data.multidb.MysqlClientProvider;
import site.itprohub.javelin.data.multidb.SqlServerClientProvider;

public class DbConnManagerTest {

    @BeforeEach
    void setup() {
        DbClientFactory.registerProvider(DatabaseClients.MYSQL, MysqlClientProvider.INSTANCE);
        DbClientFactory.registerProvider(DatabaseClients.SQLSERVER, SqlServerClientProvider.INSTANCE);
    }

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
        DbConfig mockConfig = DbConnManager.getAppDbConfig("test");

        ConnectionInfo conn = new ConnectionInfo(mockConfig);

        DbContext dbContext = new DbContext(conn);
        dbContext.openConnection();
        assertNotNull(dbContext);
        assertNotNull(dbContext.getConnection());
    }

    @Test
    void testCreateTenantDb() {
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> new DbContext(null)
        );
        
    }
}
