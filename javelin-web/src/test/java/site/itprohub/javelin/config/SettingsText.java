package site.itprohub.javelin.config;

import org.junit.jupiter.api.Test;
import site.itprohub.javelin.base.config.*;
import site.itprohub.javelin.clients.serviceClients.MoonClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SettingsText {
    @Test
    public void getSetting() {
        ConfigClient.Instance.setClient(MoonClient.Instance);

        String value = Settings.getSetting("test");

        assertEquals("test", value);
    }
}
