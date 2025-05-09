package site.itprohub.javelin.data.command;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class BeanPropertyRowMapper {
    public static <T> T mapRow(ResultSet rs, Class<T> clazz) throws Exception {
        T instance = createInstance(clazz);
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            Object value = rs.getObject(i);

            // 转换为 Java 驼峰属性名（如 last_disconnect_time → lastDisconnectTime）
            String fieldName = toCamelCase(columnName);

            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true); // 设置可访问
                field.set(instance, value); // 设置值
            } catch (NoSuchFieldException e) {
                // 跳过不匹配的字段
            }
            
        }
        return instance;
    }

    private static <T> T createInstance(Class<T> clazz) throws Exception {
        try {
            Constructor<T> ctor = clazz.getDeclaredConstructor();
            ctor.setAccessible(true); // 允许访问私有构造函数
            return ctor.newInstance();
        } catch (NoSuchMethodException e) {
           throw new RuntimeException("未找到无参构造函数: " + clazz.getName()); 
        }
    }


    private static String toCamelCase(String input) {
        StringBuilder result = new StringBuilder();
        boolean nextUpperCase = false; 
        for (char c : input.toCharArray()) {
            if (c == '_') {
                nextUpperCase = true; // 下一个字符大写 
            } else {
                result.append(nextUpperCase ? Character.toUpperCase(c) : c); // 大写
                nextUpperCase = false; // 重置
            }
        }

        return result.toString();
    }

}
