package site.itprohub.javelin.config;

import org.junit.jupiter.api.Test;
import site.itprohub.javelin.base.config.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SettingsText {
    @Test
    public void getSetting() {
        String value = Settings.getSetting("123");

        assertEquals(value, null);
    }
}
