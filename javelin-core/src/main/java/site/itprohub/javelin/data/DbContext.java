package site.itprohub.javelin.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import site.itprohub.javelin.data.command.CPQueryFactory;
import site.itprohub.javelin.data.entity.EntityFactory;

public class DbContext implements AutoCloseable {
    private DataSource dataSource;

    private Connection conn;

    private CPQueryFactory queryFactory;

    private EntityFactory entityFactory;

    public DbContext(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void openConnection() throws SQLException {
        if ( conn != null && conn.isClosed() == false ) {
            return;
        }

        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            throw new SQLException("Failed to open connection", e);
        }
    }

    public CPQueryFactory CPQuery() {
        if (queryFactory == null) {
            queryFactory = new CPQueryFactory(this);
        }

        return queryFactory;
    }

    public EntityFactory Entity() {
        if (entityFactory == null) {
            entityFactory = new EntityFactory(this);
       
        }
        return entityFactory;
    }

    public Connection getConnection() {
        return conn; 
    }

    public static DbContext create(DataSource dataSource) throws SQLException {
        return new DbContext(dataSource);
    }

    // public CPQuery creatQuery(String sql) {
    //     return queryFactory.create(sql);
    // }

    // public CPQuery creatQuery(String sql, Map<String, Object> params) {
    //     return new CPQuery(conn, sql, params);
    // }


    @Override
    public void close() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
    
}
