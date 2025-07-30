package site.itprohub.javelin.data.entity;

import site.itprohub.javelin.data.context.DbContext;
import site.itprohub.javelin.data.command.CPQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityCudUtils {

    private static final Map<String, String> insertSqlCache = new ConcurrentHashMap<>();

    public static <T> CPQuery getInsertQuery(T entity, DbContext dbContext) {
        checkArgs(entity, dbContext);

        String sql = getInsertSQL(entity.getClass(), dbContext);
        Object[] params = getInsertParams(entity, dbContext);
        return dbContext.CPQuery().create(sql, params);
    }

    public static <T> CPQuery getUpdateQuery(T entity, DbContext dbContext) {
        checkArgs(entity, dbContext);

        EntityDescription desc = EntityDescriptionCache.get(entity.getClass());
        List<ColumnInfo> columns = desc.getInsertColumns(false);
        ColumnInfo pk = desc.getPrimaryKey();

        if (pk == null) {
            throw new IllegalStateException("实体类未定义主键：" + entity.getClass().getName());
        }

        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(dbContext.clientProvider.getObjectFullName(desc.getTableName())).append(" SET ");

        List<Object> paramList = new ArrayList<>();
        for (ColumnInfo col : columns) {
            if (!col.isPrimaryKey()) {
                sql.append(dbContext.clientProvider.getObjectFullName(col.getDbName())).append(" = ?, ");
                paramList.add(col.getValue(entity));
            }
        }

        sql.setLength(sql.length() - 2); // 去掉最后的逗号
        sql.append(" WHERE ").append(pk.getDbName()).append(" = ?");
        paramList.add(pk.getValue(entity));

        return dbContext.CPQuery().create(sql.toString(), paramList.toArray());
    }

    public static <T> String getInsertSQL(Class<T> clazz, DbContext dbContext) {
        String key = clazz.getName() + "::" + dbContext.getDatabaseType();

        return insertSqlCache.computeIfAbsent(key, k -> {
            EntityDescription desc = EntityDescriptionCache.get(clazz);
            List<ColumnInfo> columns = desc.getInsertColumns();

            StringBuilder fields = new StringBuilder();
            StringBuilder values = new StringBuilder();

            for (ColumnInfo col : columns) {
                fields.append(dbContext.clientProvider.getObjectFullName(col.getDbName())).append(", ");
                values.append("?, ");
            }

            fields.setLength(fields.length() - 2);
            values.setLength(values.length() - 2);

            return String.format("INSERT INTO %s (%s) VALUES (%s)",
                    dbContext.clientProvider.getObjectFullName(desc.getTableName()),
                    fields,
                    values);
        });
    }

    public static <T> Object[] getInsertParams(T entity, DbContext dbContext) {
        EntityDescription desc = EntityDescriptionCache.get(entity.getClass());
        List<ColumnInfo> columns = desc.getInsertColumns();

        List<Object> paramList = new ArrayList<>();
        for (ColumnInfo col : columns) {
            paramList.add(col.getValue(entity));
        }
        return paramList.toArray();
    }

    private static void checkArgs(Object entity, DbContext dbContext) {
        if (entity == null) throw new IllegalArgumentException("实体不能为空");
        if (dbContext == null) throw new IllegalArgumentException("DbContext不能为空");
    }
}
