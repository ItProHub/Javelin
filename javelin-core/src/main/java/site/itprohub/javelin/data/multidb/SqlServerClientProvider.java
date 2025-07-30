package site.itprohub.javelin.data.multidb;

import site.itprohub.javelin.data.DatabaseType;

public class SqlServerClientProvider extends BaseClientProvider {

    public static final BaseClientProvider INSTANCE = new SqlServerClientProvider();

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.SQLSERVER;
    }

}
