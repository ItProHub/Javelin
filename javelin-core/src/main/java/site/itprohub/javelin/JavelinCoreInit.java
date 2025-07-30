package site.itprohub.javelin;

import site.itprohub.javelin.data.multidb.DatabaseClients;
import site.itprohub.javelin.data.multidb.DbClientFactory;
import site.itprohub.javelin.data.multidb.MysqlClientProvider;
import site.itprohub.javelin.data.multidb.SqlServerClientProvider;

public class JavelinCoreInit {
    private static boolean s_dalInited = false;
    public static void initDAL() {
        if (s_dalInited) {
            return;
        }

        // 初始化数据访问层
        autoRegisterDbProvider();

        s_dalInited = true;
    }


    private static void autoRegisterDbProvider() {
        // 自动注册数据库提供者
        DbClientFactory.registerProvider(DatabaseClients.MYSQL, MysqlClientProvider.INSTANCE);

        DbClientFactory.registerProvider(DatabaseClients.SQLSERVER, SqlServerClientProvider.INSTANCE);
    }


}
