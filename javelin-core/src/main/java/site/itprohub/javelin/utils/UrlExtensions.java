package site.itprohub.javelin.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlExtensions {
    public static Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return params;
        }
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if (idx > 0) {
                String key = decode(pair.substring(0, idx));
                String value = decode(pair.substring(idx + 1));
                params.put(key, value);
            }            

        }

        return params;
    }

    public static String decode(String s) {
        try {
            return URLDecoder.decode(s, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding is not supported", e);
        }
    }


    public static Pattern compilePathPattern(String rawPath) {
        // 解析路径参数并生成正则表达式
        // 例如，将 "/users/{id}" 转换为 "^/users/([^/]+)$"
        String regex = rawPath.replaceAll("\\{(\\w+)}", "(?<$1>[^/]+)");
        return Pattern.compile(regex);
    }

    public static List<String> extractPathVaribleNames(String rawPath) {
        List<String> names = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\{(\\w+)}").matcher(rawPath);
        while (matcher.find()) {
            names.add(matcher.group(1));
        }

        return names;
    }

}
