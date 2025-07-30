package site.itprohub.javelin.data.multidb;

import site.itprohub.javelin.data.DatabaseType;

public class MysqlClientProvider extends BaseClientProvider {

    public static final BaseClientProvider INSTANCE = new MysqlClientProvider();

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.MYSQL;
    }

    public String getObjectFullName(String name) {
        return "`" + name + "`";
    }
}
