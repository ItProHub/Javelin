package site.itprohub.javelin.config;

import org.junit.jupiter.api.Test;
import site.itprohub.javelin.base.config.*;
import site.itprohub.javelin.clients.serviceClients.MoonClient;
import site.itprohub.javelin.data.multidb.DatabaseClients;
import site.itprohub.javelin.data.multidb.DbClientFactory;
import site.itprohub.javelin.data.multidb.MysqlClientProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SettingsText {
    @Test
    public void getSetting() {
        ConfigClient.Instance.setClient(MoonClient.Instance);
        DbClientFactory.registerProvider(DatabaseClients.MYSQL, MysqlClientProvider.INSTANCE);

        String value = Settings.getSetting("test");

        assertEquals("test", value);
    }
}
