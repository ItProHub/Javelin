package site.itprohub.javelin.data.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import site.itprohub.javelin.annotations.data.DbEntity;

public class EntityDescription {
    private final String tableName;
    private final Map<String, ColumnInfo> columnMap;
    private final List<ColumnInfo> columnList;
    private ColumnInfo primaryKey;

    public EntityDescription(Class<?> clazz, Map<String, ColumnInfo> columns) {
        this.columnMap = columns;
        this.columnList = new ArrayList<>(columns.values());

        DbEntity tableAnno = clazz.getAnnotation(DbEntity.class);
        this.tableName = tableAnno.tableName();

        for (ColumnInfo col : columnList) {
            if (col.isPrimaryKey()) {
                if (this.primaryKey != null)
                    throw new IllegalStateException("实体类只能定义一个主键字段: " + clazz.getName());
                this.primaryKey = col;
            }
        }
    }

    public String getTableName() {
        return tableName;
    }

    public ColumnInfo getPrimaryKey() {
        return primaryKey;
    }

    public List<ColumnInfo> getInsertColumns() {
        return columnList;
    }

    public List<ColumnInfo> getInsertColumns(boolean includePk) {
        return columnList.stream()
                .filter(col -> includePk || !col.isPrimaryKey())
                .collect(Collectors.toList());
    }

    public List<ColumnInfo> getAllColumns() {
        return columnList;
    }

    public ColumnInfo getColumn(String dbName) {
        return columnMap.get(dbName);
    }
}


