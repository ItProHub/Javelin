package site.itprohub.javelin.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import site.itprohub.javelin.data.command.CPQueryFactory;

public class DbContext implements AutoCloseable {
    private final Connection conn;

    private CPQueryFactory queryFactory;

    public DbContext(Connection conn) {
        this.conn = conn;
    }

    public CPQueryFactory CPQuery() {
        if (queryFactory == null) {
            queryFactory = new CPQueryFactory(this);
        }

        return queryFactory;
    }

    public Connection getConnection() {
        return conn; 
    }

    public static DbContext create(String connStr) throws SQLException {
        Connection conn = DriverManager.getConnection(connStr);
        return new DbContext(conn);
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
