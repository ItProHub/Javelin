package site.itprohub.javelin.clients.serviceClients;

import site.itprohub.javelin.common.ResourceLock;
import site.itprohub.javelin.utils.StringExtensions;
import site.itprohub.javelin.data.config.DbConfigUtils;
import site.itprohub.javelin.data.context.DbContext;

import java.sql.SQLException;
import java.time.Instant;

import site.itprohub.javelin.base.cache.CacheHashMap;
import site.itprohub.javelin.base.config.IConfigClient;
import site.itprohub.javelin.data.config.DbConfig;

// 配置服务客户端
public class MoonClient implements IConfigClient {

    public static final MoonClient Instance = new MoonClient();

    private final CacheHashMap<String> settingsCache = new CacheHashMap<>();
    private final CacheHashMap<DbConfig> dbConfigCache = new CacheHashMap<>();
    private final CacheHashMap<DbConfig> tntDbConfigCache = new CacheHashMap<>();

    private final ResourceLock settingsLock = new ResourceLock();

    @Override
    public String getSetting(String name) {
        String value = settingsCache.get(name);

        if (value == null) {
            synchronized (settingsLock.getLock(name)) { // 防止多个请求同时进入
                value = settingsCache.get(name); // 再次检查

                if (value == null) {
                    value = getSettingByDb(name);
                }
            }
        }

        return value;
    }

    @Override
    public DbConfig getAppDbConfig(String name) {
        DbConfig value = dbConfigCache.get(name);

        if (value == null) {
            synchronized (settingsLock.getLock(name)) { // 防止多个请求同时进入
                value = dbConfigCache.get(name); // 再次检查

                if (value == null) {
                    value = getDbConfig(name);
                    

                }
            }
        }

        return value;
    }

    @Override
    public DbConfig getTntDbConfig(String tenantId){
        DbConfig value = tntDbConfigCache.get(tenantId);

        if (value == null) {
            synchronized (settingsLock.getLock(tenantId)) { // 防止多个请求同时进入
                value = tntDbConfigCache.get(tenantId); // 再次检查

                if (value == null) {
                    value = getTntDbConfigFromDb(tenantId);
                }
            }
        }

        return value;
    }

    private String getSettingByDb(String name) {
        try{
            try ( DbContext db = DbConfigUtils.CreateConfigDbContext() ) {
                String value = db.CPQuery().create("select value from settings where name = ?", name).executeScalar(String.class);
                if (StringExtensions.isNullOrEmpty(value) == false) {
                    settingsCache.set(name, value, Instant.now().plusSeconds(MoonClientOptions.settingsCacheSeconds)); // 可选：写入缓存
                }

                return value;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        
        
    }

    private DbConfig getDbConfig(String name) {
        try ( DbContext db = DbConfigUtils.CreateConfigDbContext() ) {
            DbConfig config = db.CPQuery().create("select * from dbconfig where name = ?", name).executeScalar(DbConfig.class);
            if (config != null) {
                dbConfigCache.set(name, config, Instant.now().plusSeconds(MoonClientOptions.settingsCacheSeconds)); // 可选：写入缓存
            }

            return config;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return null;
        }
    }

    private DbConfig getTntDbConfigFromDb(String tenantId) {
        try ( DbContext db = DbConfigUtils.CreateConfigDbContext() ) {
            DbConfig config = db.CPQuery().create("select * from dbconfig where name = ?", tenantId).executeScalar(DbConfig.class);
            if (config != null) {
                tntDbConfigCache.set(tenantId, config, Instant.now().plusSeconds(MoonClientOptions.settingsCacheSeconds)); // 可选：写入缓存
            }

            return config;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return null;
        }
    }

}
