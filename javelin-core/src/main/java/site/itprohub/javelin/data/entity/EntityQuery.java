package site.itprohub.javelin.data.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import site.itprohub.javelin.data.context.DbContext;

public class EntityQuery<T> {
    private final DbContext dbContext;
    private final Class<T> entityType;

    private final List<String> whereClauses = new ArrayList<>();
    private final List<Object> whereParams = new ArrayList<>();

    public EntityQuery(DbContext dbContext, Class<T> entityType) {
        this.dbContext = dbContext;
        this.entityType = entityType;
    }

    public EntityQuery<T> where(String clause, Object... params) {
        this.whereClauses.add(clause);
        Collections.addAll(whereParams, params);
        return this;
    }

    public T toSingle() {
        String sql = buildSql();
        return dbContext.CPQuery().create(sql, whereParams.toArray()).toSingle(entityType);
    }

    public List<T> toList() {
        String sql = buildSql();
        return dbContext.CPQuery().create(sql, whereParams.toArray()).toList(entityType);
    }

    private String buildSql() {
        EntityDescription desc = EntityDescriptionCache.get(entityType);
        String tableName = desc.getTableName(); // 可用注解扩展
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(tableName);
        if (!whereClauses.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", whereClauses));
        }
        return sql.toString();
    }
    
    public int deleteByKey(Object key) {
        return EntityCurdUtils.getDeleteQuery(entityType, key, dbContext).executeNonQuery();
    }

    public T getByKey(Object key) {
        return EntityCurdUtils.getSelectQuery(entityType, key, dbContext).toSingle(entityType);
    }
}

