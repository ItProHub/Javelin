package site.itprohub.javelin.data.config;

import site.itprohub.javelin.data.context.DbContext;
import site.itprohub.javelin.data.multidb.DatabaseClients;

public class DbConfig  implements Cloneable {
    public String dbType;

    public String server;

    public Integer port = 3306;

    public String dbName;

    public String dbUser;

    public String dbPassword;

    public String args;    
    
    private String driver = DatabaseClients.MYSQL;

    private int maxPoolSize = 10; // Default value for maxPoolSize

    private int minIdle = 5; // 连接池中保持的最小空闲连接数量

    private long maxIdleTime = 60000; // Default value for maxIdleTime (1 minute)

    private long connectionTimeout = 30000L; // Default value for connectionTimeout (30 seconds)


    public String getUrl() {
        port = port == null ? 3306 : port;
        return "jdbc:" + dbType + "://" + server + ":" + port + "/" + dbName; 
    }

    public String getConnectionString() {
        port = port == null ? 3306 : port;
        return "jdbc:" + dbType + "://" + server + ":" + port + "/" + dbName + "?user=" + dbUser + "&password=" + dbPassword; 
    }

    public String getUsername(){
        return dbUser;
    }

    public String getPassword(){
        return dbPassword;
    }

    public String getDriverClassName(){
        if(dbType.equals("mysql"))
            return DatabaseClients.MYSQL;
        else if(dbType.equals("sqlserver"))
            return DatabaseClients.SQLSERVER;
        else
            return driver;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public long getConnectionTimeout() {
        return connectionTimeout; 
    }

     @Override
    public DbConfig clone() {
        try {
            return (DbConfig) super.clone();  // 浅拷贝
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();  // 不可能发生
        }
    }
}
