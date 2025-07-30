package site.itprohub.javelin.data.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import site.itprohub.javelin.annotations.data.DbColumn;
import site.itprohub.javelin.annotations.data.DbEntity;

public class EntityDescriptionCache {
    private static final Map<Class<?>, EntityDescription> cache = new ConcurrentHashMap<>();

    public static EntityDescription get(Class<?> clazz) {
        return cache.computeIfAbsent(clazz, key -> create(clazz));
    }

    private static EntityDescription create(Class<?> clazz) {
        if(!clazz.isAnnotationPresent(DbEntity.class)) {
            throw new IllegalArgumentException("实体缺少 @DbEntity 注解: " + clazz.getName());
        }

        Map<String, ColumnInfo> columnMap = new LinkedHashMap<>();
        int index = -1;

        for (Field field: clazz.getDeclaredFields()) {
            index++;
            
            // 排除静态字段或final字段
            if(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                continue;
            }

            // 没有 @DbColumn 注解的字段，默认不处理
            DbColumn attr = field.getAnnotation(DbColumn.class);
            if(!field.isAnnotationPresent(DbColumn.class)) {
                continue;
            }

            // 支持类型判断
            Class<?> type = field.getType();
            if(!isSupportedType(type) && attr.field().isEmpty()) {
                continue;
            }

            ColumnInfo col = new ColumnInfo(field, attr, index);
            columnMap.put(col.getDbName(), col);

        }

        if (columnMap.isEmpty()) {
            throw new IllegalArgumentException("实体类没有任何有效字段: " + clazz.getName());
        }


        return new EntityDescription(clazz, columnMap);
    }


    private static boolean isSupportedType(Class<?> type) {
        // 可扩展的类型判断，也可与 TypeList 配合
        return type.isPrimitive()
                || type == String.class
                || type == Integer.class
                || type == Long.class
                || type == Short.class
                || type == Boolean.class
                || type == Double.class
                || type == Float.class
                || type == Byte.class
                || type == java.math.BigDecimal.class
                || type == java.util.Date.class
                || type == java.sql.Date.class
                || type == java.sql.Timestamp.class
                || type == UUID.class
                || type == byte[].class
                || type.isEnum();
    }

}
