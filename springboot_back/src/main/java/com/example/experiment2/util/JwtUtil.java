package com.example.experiment2.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire-hours:24}")
    private long expireHours;

    public String generateToken(Long userId, String username, String role) {
        try {
            Map<String, Object> header = new LinkedHashMap<>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");

            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("userId", userId);
            payload.put("username", username);
            payload.put("role", role);
            payload.put("exp", Instant.now().plusSeconds(expireHours * 3600).toEpochMilli());

            String headerText = encodeJson(header);
            String payloadText = encodeJson(payload);
            String content = headerText + "." + payloadText;
            return content + "." + sign(content);
        } catch (Exception e) {
            throw new IllegalStateException("生成 token 失败", e);
        }
    }

    public Claims parseToken(String token) {
        try {
            String[] parts = token == null ? new String[0] : token.split("\\.");
            if (parts.length != 3) {
                return null;
            }
            String content = parts[0] + "." + parts[1];
            if (!sign(content).equals(parts[2])) {
                return null;
            }

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            Map<String, Object> payload = OBJECT_MAPPER.readValue(payloadJson, new TypeReference<Map<String, Object>>() {});
            long exp = ((Number) payload.get("exp")).longValue();
            if (exp < Instant.now().toEpochMilli()) {
                return null;
            }

            Claims claims = new Claims();
            claims.setUserId(((Number) payload.get("userId")).longValue());
            claims.setUsername(String.valueOf(payload.get("username")));
            claims.setRole(String.valueOf(payload.get("role")));
            return claims;
        } catch (Exception e) {
            return null;
        }
    }

    private String encodeJson(Map<String, Object> data) throws Exception {
        byte[] json = OBJECT_MAPPER.writeValueAsBytes(data);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(json);
    }

    private String sign(String content) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(content.getBytes(StandardCharsets.UTF_8)));
    }

    public static class Claims {
        private Long userId;
        private String username;
        private String role;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
