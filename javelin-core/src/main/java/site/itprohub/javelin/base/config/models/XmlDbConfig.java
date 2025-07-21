package site.itprohub.javelin.base.config.models;

import jakarta.xml.bind.annotation.*;
import site.itprohub.javelin.data.config.DbConfig;

public class XmlDbConfig {
    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "DbType")
    public String dbType;

    @XmlElement(name = "server")
    public String server;

    @XmlElement(name = "port")
    public Integer port;

    @XmlElement(name = "database")
    public String database;

    @XmlElement(name = "uid")
    public String userName;

    @XmlElement(name = "pwd")
    public String password;

    @XmlElement(name = "args")
    public String args;

    public String toString() {
        return String.format("%s/%s/%s",
            dbType, server,  database);
    }

    public DbConfig toDbConfig() {
        DbConfig config = new DbConfig();

        config.dbType = dbType;
        config.server = server;
        config.port = port;
        config.dbName = database;
        config.dbUser = userName;
        config.dbPassword = password;
        config.args = args;

        return config;
    }
}
