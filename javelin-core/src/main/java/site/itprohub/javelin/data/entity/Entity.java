package site.itprohub.javelin.data.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import site.itprohub.javelin.annotations.data.DbColumn;
import site.itprohub.javelin.annotations.data.DbEntity;
import site.itprohub.javelin.data.DbContext;

public class Entity<T> implements IEntity<T> {
    
    private final Class<T> entityType;

    private final DbContext dbContext;

    public Entity(Class<T> entityType, DbContext dbContext) {
        this.entityType = entityType;
        this.dbContext = dbContext;
    }

    @Override
    public T findById(int id) {
        String tableName = getTableName();
        String pkColumn = getIdColumn();

        String sql = String.format("SELECT * FROM %s WHERE %s = ?", tableName, pkColumn);        
        
        return dbContext.CPQuery().create(sql, id).toSingle(entityType);
    }

    @Override
    public int insert(T entity) {
        String tableName = getTableName();
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        for ( Field field : entityType.getDeclaredFields() ) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            DbColumn column = field.getAnnotation(DbColumn.class);
            if (column == null) {
                continue;
            }
            String columnName = column != null && !column.field().isEmpty() ? column.field() : field.getName();

            field.setAccessible(true);
            try {
                Object value = field.get(entity);
                if (value != null) {
                    columns.add(columnName);
                    values.add(value);
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        String cols = String.join(", ", columns);
        String placeholders = columns.stream().map(col -> "?").collect(Collectors.joining(", "));

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, cols, placeholders);

        return dbContext.CPQuery().create(sql, values.toArray()).executeNonQuery();
    }

    @Override
    public int update(T entity) {
        String tableName = getTableName();
        List<String> setClauses = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        String pkColumn = null;
        Object pkValue = null;

        for ( Field field : entityType.getDeclaredFields() ) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            DbColumn column = field.getAnnotation(DbColumn.class);
            if (column == null) {
                continue;
            }
            String columnName = column!= null &&!column.field().isEmpty()? column.field() : field.getName();
            field.setAccessible(true);

            try {
                Object value = field.get(entity);

                if ( column.isPrimaryKey() ) {
                    pkColumn = columnName;
                    pkValue = value; 
                 } else {
                    setClauses.add(columnName + " = ?");
                    values.add(value);
                 }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e); 
            }            
        }
        if (pkColumn == null || pkValue == null) {
            throw new IllegalArgumentException("主键字段未设置，无法执行 update"); 
        }

        // 主键参数最后添加  update xxx set a=? where id=?
        values.add(pkValue);

        String setStr = String.join(", ", setClauses);
        String sql = String.format("UPDATE %s SET %s WHERE %s =?", tableName, setStr, pkColumn);

        return dbContext.CPQuery().create(sql, values.toArray()).executeNonQuery();
    }

    @Override
    public int delete(int id) {
        String tableName = getTableName();
        String pkColumn = getIdColumn();

        String sql = String.format("DELETE FROM %s WHERE %s =?", tableName, pkColumn);

        return dbContext.CPQuery().create(sql, id).executeNonQuery();
    }


    private String getTableName() {
        DbEntity entity = entityType.getAnnotation(DbEntity.class);
        if (entity == null || entity.tableName().isEmpty()) {
            throw new IllegalArgumentException("Entity must be annotated with @DbEntity and specify a table name");
        }

        return entity.tableName();
    }

    private String getIdColumn() {
        for ( Field field : entityType.getDeclaredFields() ) {
            DbColumn column = field.getAnnotation(DbColumn.class);

            if (column != null && column.isPrimaryKey()) {
                return !column.field().isEmpty() ? column.field() : field.getName();
            } 
        }
        throw new IllegalArgumentException("Entity must have an @Id annotated field");
    }


    //#region linq 方法

    private final List<String> whereClauses = new ArrayList<>();
    private final List<Object> whereParams = new ArrayList<>();

    public Entity<T> where( String sql, Object... params) {
         this.whereClauses.add(sql);
         Collections.addAll(whereParams, params);
         return this;
    }

    public T toSingle() {
        String tableName = getTableName();
        String where = buildWhereClause();

        String sql = String.format("SELECT * FROM %s %s", tableName, where);

        return dbContext.CPQuery().create(sql, whereClauses).toSingle(entityType);
    }

    public List<T> toList() {
        String tableName = getTableName();
        String where = buildWhereClause();

        String sql = String.format("SELECT * FROM %s %s", tableName, where);

        return dbContext.CPQuery().create(sql , whereClauses).toList(entityType);
    }

    private String buildWhereClause() {
        if (whereClauses.isEmpty()) {
            return "";
        }
        return "WHERE " + String.join(" AND ", whereClauses);
    }

    //#endregion


}
