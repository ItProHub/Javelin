package site.itprohub.javelin.common;

import java.util.concurrent.ConcurrentHashMap;

public class ResourceLock {
    private final ConcurrentHashMap<String, Object> lockMap = new ConcurrentHashMap<>();

    public Object getLock(String key) {
        return lockMap.computeIfAbsent(key, k -> new Object());
    }
}
