package cn.hush.domain.auth.service;

import com.google.common.util.concurrent.AbstractService;
import io.jsonwebtoken.Claims;

/**
 * @author Hush
 * @description 鉴权服务
 * @create 2025-01-10 下午9:18
 */

public class AuthService extends AbstractAuthService {

    @Override
    public boolean checkToken(String token) {
        return isVerify(token);
    }

    @Override
    public String openid(String token) {
        Claims claims = decode(token);
        return claims.get("openId").toString();
    }
}
