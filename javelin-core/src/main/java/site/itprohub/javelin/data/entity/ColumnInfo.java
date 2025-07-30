package site.itprohub.javelin.data.entity;

import java.lang.reflect.Field;

import site.itprohub.javelin.annotations.data.DbColumn;

public class ColumnInfo {

    private final Field field;
    private final DbColumn attr;
    private final String dbName;
    private Class<?> dataType;
    private int index;

    public ColumnInfo(Field field, DbColumn attr, int index) {
        if (field == null)
            throw new IllegalArgumentException("field 不能为空");

        this.field = field;
        this.attr = attr;
        this.index = index;

        this.dbName = (attr == null || attr.field().isEmpty())
                ? field.getName()
                : attr.field();

        this.dataType = resolveActualType(field.getType());
        field.setAccessible(true);
    }

    public String getDbName() {
        return dbName;
    }

    public Field getField() {
        return field;
    }

    public DbColumn getAttr() {
        return attr;
    }

    public int getIndex() {
        return index;
    }

    public Class<?> getDataType() {
        return dataType;
    }

    public boolean isPrimaryKey() {
        return attr != null && attr.isPrimaryKey();
    }

    public Object getValue(Object entity) {
        try {
            return field.get(entity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("读取字段失败: " + field.getName(), e);
        }
    }

    public void setValue(Object entity, Object value) {
        try {
            field.set(entity, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("设置字段失败: " + field.getName(), e);
        }
    }

    private static Class<?> resolveActualType(Class<?> type) {
        if (type.equals(Integer.class)) return int.class;
        if (type.equals(Long.class)) return long.class;
        if (type.equals(Boolean.class)) return boolean.class;
        if (type.equals(Double.class)) return double.class;
        if (type.equals(Float.class)) return float.class;
        if (type.equals(Short.class)) return short.class;
        if (type.equals(Byte.class)) return byte.class;
        return type;
    }
}
