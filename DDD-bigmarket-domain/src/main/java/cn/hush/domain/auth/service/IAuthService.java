package cn.hush.domain.auth.service;

/**
 * @author Hush
 * @description 认证
 * @create 2025-01-10 下午9:18
 */
public interface IAuthService {

    boolean checkToken(String token);

    String openid(String token);

}
