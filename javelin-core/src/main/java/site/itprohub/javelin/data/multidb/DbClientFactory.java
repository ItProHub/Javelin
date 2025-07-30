package site.itprohub.javelin.data.multidb;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DbClientFactory {

    private static final Map<String, BaseClientProvider> PROVIDERS = new ConcurrentHashMap<>();

    private DbClientFactory() {
        // 禁止实例化
    }

    /**
     * 注册客户端提供者
     */
    public static void registerProvider(String providerName, BaseClientProvider provider) {
        if (providerName == null || providerName.isEmpty()) {
            throw new IllegalArgumentException("providerName 不能为空");
        }

        if (provider == null) {
            throw new IllegalArgumentException("provider 实例不能为空");
        }

        PROVIDERS.put(providerName, provider);
        System.out.println("[INFO] Register DbClient Provider: " + providerName + " => " + provider.getClass().getName());

    }

    /**
     * 根据 providerName 获取 BaseClientProvider 实例
     */
    public static BaseClientProvider getProvider(String providerName) {

        BaseClientProvider result = PROVIDERS.get(providerName);
        if (result != null) {
            return result;
        }

        throw new UnsupportedOperationException("不支持的数据提供者类型：" + providerName);
    }
   
}
