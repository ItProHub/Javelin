package site.itprohub.javelin.data.command;

import java.util.Arrays;

import site.itprohub.javelin.data.DbContext;

public class CPQueryFactory {
    private final DbContext dbContext;

    public CPQueryFactory(DbContext dbContext) {
        this.dbContext = dbContext;
    }

    public CPQuery create(String sql) {
        CPQuery query = new CPQuery(dbContext);
        query.init(sql, null);
        return query;
    }

    public CPQuery create(String sql, Object... params) {
        CPQuery query = new CPQuery(dbContext);
        query.init(sql, Arrays.asList(params));
        return query;
    }
}
