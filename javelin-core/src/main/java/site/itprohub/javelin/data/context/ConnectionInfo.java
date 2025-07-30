package site.itprohub.javelin.data.context;

import site.itprohub.javelin.data.config.DbConfig;
import site.itprohub.javelin.data.multidb.DatabaseClients;

public class ConnectionInfo {
    public String connectionString;

    private String providerName;

    public ConnectionInfo(String connectionString) {
        if (connectionString.isEmpty()) {
            throw new IllegalArgumentException("connectionString is empty");
        }
        this.connectionString = connectionString;

        this.providerName = getProviderName();
    }

    public ConnectionInfo(DbConfig config) {
        this(config.getConnectionString());
    }

    public String getProviderName(){
        if (connectionString.startsWith("jdbc:mysql:")) return DatabaseClients.MYSQL;
        if (connectionString.startsWith("jdbc:postgresql:")) return "postgresql";
        if (connectionString.startsWith("jdbc:sqlserver:")) return DatabaseClients.MYSQL;
        if (connectionString.startsWith("jdbc:oracle:")) return "oracle";
        return DatabaseClients.MYSQL;
    }

    public String toString() {
        return String.format("%s\n%s", providerName, connectionString);
    }
}
