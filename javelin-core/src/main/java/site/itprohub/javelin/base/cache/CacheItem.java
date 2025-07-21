package site.itprohub.javelin.base.cache;

import java.lang.ref.WeakReference;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

public final class CacheItem<T> implements AutoCloseable {

    private T strongRef;
    private WeakReference<T> weakRef;
    private final Object lock = new Object();
    private final boolean useWeakRef;
    private final AtomicLong expirationTime = new AtomicLong(0);

    public static final long NEVER_EXPIRE = Long.MAX_VALUE;

    public CacheItem(T value, Instant expireAt, boolean useWeakRef) {
        this.useWeakRef = useWeakRef;
        setValue(value, expireAt);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expirationTime.get();
    }

    public void set(T value, Instant expireAt) {
        setValue(value, expireAt);
    }

    private void setValue(T value, Instant expireAt) {
        synchronized (lock) {
            if (value == null) {
                strongRef = null;
                weakRef = null;
                expirationTime.set(0);
                return;
            }

            if (shouldUseWeakReference(value, expireAt)) {
                strongRef = null;
                weakRef = new WeakReference<>(value);
            } else {
                strongRef = value;
                weakRef = null;
            }

            expirationTime.set(safeToEpochMilli(expireAt));
        }
    }

    private long safeToEpochMilli(Instant instant) {
        try {
            return instant.toEpochMilli();
        } catch (ArithmeticException e) {
            return NEVER_EXPIRE;
        }
    }

    private boolean shouldUseWeakReference(T value, Instant expireAt) {
        if (!useWeakRef) return false;

        // 永不过期的不使用弱引用
        if (safeToEpochMilli(expireAt) == NEVER_EXPIRE) return false;

        // 小字符串不使用弱引用
        if (value instanceof String str && str.length() < 1024) return false;

        return true;
    }

    public T get() {
        synchronized (lock) {
            if (isExpired()) {
                close();
                return null;
            }

            if (strongRef != null) return strongRef;
            if (weakRef != null) return weakRef.get();

            return null;
        }
    }

    @Override
    public void close() {
        synchronized (lock) {
            if (strongRef instanceof AutoCloseable ac) {
                try { ac.close(); } catch (Exception ignored) {}
            }
            strongRef = null;

            if (weakRef != null) {
                T ref = weakRef.get();
                if (ref instanceof AutoCloseable ac) {
                    try { ac.close(); } catch (Exception ignored) {}
                }
                weakRef.clear();
                weakRef = null;
            }
        }
    }
}
