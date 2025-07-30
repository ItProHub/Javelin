package site.itprohub.javelin.base.cache.init;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ApplicationInitializerTest {

    @Test
    public void testInit()
    {
        int counter = AppInitializer.counter;
        AppInitializer.Init();
        assertEquals(counter+ 1, AppInitializer.counter);
    }
}
