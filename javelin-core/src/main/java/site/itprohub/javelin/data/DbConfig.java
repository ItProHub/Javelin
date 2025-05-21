package site.itprohub.javelin.data;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConfig {
    public String dbType;

    public String server;

    public Integer port = 3306;

    public String dbName;

    public String dbUser;

    public String dbPassword;

    public String args;

    public String toString() {
        port = port == null ? 3306 : port;
        return "jdbc:" + dbType + "://" + server + ":" + port + "/" + dbName + "?user=" + dbUser + "&password=" + dbPassword + "&useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8";
    }


    public DbContext creaDbContext() {
        return new DbContext(this.toString()); 
    }
}
