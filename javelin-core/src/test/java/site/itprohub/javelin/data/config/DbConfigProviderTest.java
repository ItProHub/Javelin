package site.itprohub.javelin.data.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class DbConfigProviderTest {
    @Test
    void testGetAppDbConfig() throws SQLException {        
        DbConfig mockConfig = Mockito.mock(DbConfig.class);
        DbConfigProviderProxy.setMockConfig("test", mockConfig);

        DbConfig result = DbConfigProviderProxy.getAppDbConfig("test");

        assertNotNull(result);
        assertEquals(mockConfig, result);
    }

    @Test
    void testGetAppDbConfig_throwsException() {
        assertThrows(RuntimeException.class, () -> DbConfigProvider.getAppDbConfig("nonExistent"));
        assertThrows(IllegalArgumentException.class, () -> DbConfigProvider.getAppDbConfig(""));
    }
}
