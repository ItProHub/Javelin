package site.itprohub.javelin.base.cache;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import site.itprohub.javelin.base.cache.CacheHashMap;


public class CacheHashMapTest {
    @Test
    public void testCacheHashMap() {
        CacheHashMap<String> cache = new CacheHashMap<>();
        cache.set("key", "value");
        assertEquals("value", cache.get("key"));
        
    }

    @Test
    public void testCacheHashMapExpiration() throws InterruptedException {
        CacheHashMap<String> cache = new CacheHashMap<>();
        cache.set("key", "value", Instant.now().plusSeconds(1));
        assertEquals("value", cache.get("key"));
        Thread.sleep(2000);
        assertEquals(null, cache.get("key"));
    }

    @Test
    public void testCacheHashMapExpiration2() throws InterruptedException {
        CacheHashMap<String> cache = new CacheHashMap<>();
        cache.set("key", "value", Instant.now().plusSeconds(2));
        assertEquals("value", cache.get("key"));
        Thread.sleep(1000);
        assertEquals("value", cache.get("key"));
    }
}
