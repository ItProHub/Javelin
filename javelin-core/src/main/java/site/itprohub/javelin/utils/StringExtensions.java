package site.itprohub.javelin.utils;

public class StringExtensions {

    public static Object convertTo(String value, Class<?> targetType) {
        if (value == null) {
            return null;
        } else if (targetType == String.class) {
            return value;
        } else if (targetType == int.class || targetType == Integer.class) {
            return Integer.parseInt(value);
        } else if (targetType == long.class || targetType == Long.class) {
            return Long.parseLong(value);
        } else if (targetType == double.class || targetType == Double.class) {
            return Double.parseDouble(value);
        } else if (targetType == boolean.class || targetType == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + targetType);
        }
    }


    public static Boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

}
