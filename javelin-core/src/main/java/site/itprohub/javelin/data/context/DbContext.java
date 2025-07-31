package site.itprohub.javelin.data.context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import site.itprohub.javelin.data.DatabaseType;
import site.itprohub.javelin.data.command.CPQueryFactory;
import site.itprohub.javelin.data.config.DbConfig;
import site.itprohub.javelin.data.entity.DbBatchFactory;
import site.itprohub.javelin.data.entity.EntityFactory;
import site.itprohub.javelin.data.entity.EntityQuery;
import site.itprohub.javelin.data.multidb.BaseClientProvider;
import site.itprohub.javelin.data.multidb.DbClientFactory;

public class DbContext implements AutoCloseable {
    private ConnectionInfo connInfo;

    private Connection _connection;  

    public BaseClientProvider clientProvider;

    private boolean _isDisposed = false;

    public DatabaseType getDatabaseType() {
        return clientProvider.getDatabaseType();
    }

    public DbContext(ConnectionInfo conn) {
        if (conn == null) {
            throw new IllegalArgumentException("connInfo is null");
        }

        this.connInfo = conn;
        clientProvider = DbClientFactory.getProvider(conn.getProviderName());
    }

    public static DbContext create(DbConfig dbConfig) {
        if (dbConfig == null) {
            throw new IllegalArgumentException("dbConfig is null");
        }
        return new DbContext(new ConnectionInfo(dbConfig));
    }


    public Connection getConnection() {
        if(_isDisposed) {
            throw new RuntimeException("db connection is disposed!");
        }

        if (_connection == null) {
            throw new RuntimeException("db connection is not created!");
        }

        return _connection;
    }

    public void openConnection() throws SQLException {
        if ( _connection != null && _connection.isClosed() == false ) {
            return;
        }

        try {
            _connection = DriverManager.getConnection(this.connInfo.connectionString);
        } catch (SQLException e) {
            throw new SQLException("Failed to open connection", e);
        }
    }



    private CPQueryFactory queryFactory;

    public CPQueryFactory CPQuery() {
        if (queryFactory == null) {
            queryFactory = new CPQueryFactory(this);
        }

        return queryFactory;
    }

    private EntityFactory entityFactory;

    public EntityFactory Entity() {
        if (entityFactory == null) {
            entityFactory = new EntityFactory(this);
       
        }
        return entityFactory;
    }

    public <T> EntityQuery<T> Entity(Class<T> entityType) {
        return new EntityQuery<T>(this, entityType);
    }

    // 批量操作
    private DbBatchFactory batchFactory;
    public DbBatchFactory Batch() {
        if (batchFactory == null) {
            batchFactory = new DbBatchFactory(this);
        }
        return batchFactory;
    }

    @Override
    public void close() throws SQLException {
        _isDisposed = true;
        if (_connection != null && !_connection.isClosed()) {
            _connection.close();
        }
    }
    
}
