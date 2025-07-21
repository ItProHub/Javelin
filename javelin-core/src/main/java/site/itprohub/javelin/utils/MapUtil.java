package site.itprohub.javelin.utils;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MapUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, Object> toMap(Object obj) {
        return objectMapper.convertValue(obj, Map.class);
    }
}
