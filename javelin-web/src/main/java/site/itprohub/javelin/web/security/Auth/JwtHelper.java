package site.itprohub.javelin.web.security.Auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class JwtHelper {

    private JwtHelper() {}  // 禁止实例化

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String generateToken(String secret, Map<String, Object> claims, long expirationMillis) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims parseToken(String secret, String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static boolean isExpired(String secret, String token) {
        try {
            Claims claims = parseToken(secret, token);
            return claims.getExpiration().before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }


    public static LoginTicket decodeToken(String secret, String token) {
        try {
            Claims claims = parseToken(secret, token);
            Map<String, Object> map = new HashMap<>(claims);
            // 将 claims 转为 LoginTicket（假设字段结构兼容）
            return objectMapper.convertValue(map, LoginTicket.class);
        } catch (Exception e) {
            System.err.println("JWT decode failed: " + e.getMessage());
            return null;
        }
    }


}
