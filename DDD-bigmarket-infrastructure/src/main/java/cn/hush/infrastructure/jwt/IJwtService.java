package cn.hush.infrastructure.jwt;

import io.jsonwebtoken.Claims;

import java.util.Map;

/**
 * @author Hush
 * @description
 * @create 2025-01-07 上午4:23
 */
public interface IJwtService {

    public String createJWT(String secretKey, Long ttlMillis, Map<String, Object> claims);

    public Claims parseJWT(String secretKey, String token);

    public String buildEmpToken(Long id);
}
