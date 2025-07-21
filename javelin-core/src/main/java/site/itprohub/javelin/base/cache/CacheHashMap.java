package site.itprohub.javelin.base.cache;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class CacheHashMap<T> {

    private final ConcurrentHashMap<String, CacheItem<T>> cache;

    private final boolean useWeakRef;

    private final int expirationScanFrequencySeconds;

    private final AtomicLong lastScanTime = new AtomicLong(System.currentTimeMillis());

    private final Object lock = new Object();


    public CacheHashMap() {
        this(300, true, 60);
    }

    public CacheHashMap(int capacity) {
        this(capacity, true, 60);
    }

    public CacheHashMap(int capacity, boolean useWeakRef, int expirationScanFrequencySeconds) {
        this.cache = new ConcurrentHashMap<>(capacity);
        this.useWeakRef = useWeakRef;
        this.expirationScanFrequencySeconds = expirationScanFrequencySeconds;
    }


    public void set(String key, T value) {
        set(key, value, Instant.MAX);
    }

    public void set(String key, T value, Instant expireAt) {
        if (key == null || key.isEmpty()) throw new IllegalArgumentException("key is null or empty");
        if (key.length() > 256) throw new IllegalArgumentException("key too long");
        if (value == null) return;

        CacheItem<T> item = new CacheItem<>(value, expireAt, useWeakRef);
        cache.put(key.toLowerCase(Locale.ROOT), item);

        if (expirationScanFrequencySeconds > 0 && !expireAt.equals(Instant.MAX)) {
            checkExpiredItems();
        }
    }


    public T get(String key) {
        if (key == null || key.isEmpty()) throw new IllegalArgumentException("key is null");

        CacheItem<T> item = cache.get(key.toLowerCase(Locale.ROOT));
        return (item == null || item.isExpired()) ? null : item.get();
    }

    public void remove(String key) {
        if (key == null) return;
        CacheItem<T> item = cache.remove(key.toLowerCase(Locale.ROOT));
        if (item != null) item.close();
    }

    public void clear() {
        cache.clear();
    }

    public int size() {
        return cache.size();
    }

    public Set<String> getKeys() {
        return new HashSet<>(cache.keySet());
    }


    private void checkExpiredItems() {
        long now = System.currentTimeMillis();
        long last = lastScanTime.get();

        if (now - last >= expirationScanFrequencySeconds * 1000L) {
            synchronized (lock) {
                if (last == lastScanTime.get()) {
                    lastScanTime.set(now);
                    CompletableFuture.runAsync(this::clearExpiredItems);
                }
            }
        }
    }

     private void clearExpiredItems() {
        for (Map.Entry<String, CacheItem<T>> entry : cache.entrySet()) {
            if (entry.getValue().isExpired()) {
                cache.remove(entry.getKey());                
            }
        }
    }

}
