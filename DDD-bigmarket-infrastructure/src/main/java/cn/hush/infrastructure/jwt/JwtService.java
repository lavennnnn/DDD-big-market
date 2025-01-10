package cn.hush.infrastructure.jwt;

import cn.hush.types.common.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hush
 * @description
 * @create 2025-01-07 上午4:07
 */
@Service
public class JwtService implements IJwtService {

    @Resource
    private JwtConfigProperties jwtConfigProperties;


    /**
     * 生成jwt
     * 使用Hs256算法, 私匙使用固定秘钥
     *
     * @param secretKey jwt秘钥
     * @param ttlMillis jwt过期时间(毫秒)
     * @param claims    设置的信息
     * @return jwt字符串
     */
    public String createJWT(String secretKey, Long ttlMillis, Map<String, Object> claims) {
        // 指定签名的时候使用的签名算法，也就是header那部分
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成JWT的时间
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        // 设置jwt的body
        return Jwts.builder()
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置过期时间
                .setExpiration(exp)
                .compact();
    }

    /**
     * Token解密
     *
     * @param secretKey jwt秘钥 此秘钥一定要保留好在服务端, 不能暴露出去, 否则sign就可以被伪造, 如果对接多个客户端建议改造成多个
     * @param token     加密后的token
     * @return
     */
    public Claims parseJWT(String secretKey, String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJwt(token)
                .getBody();

        return claims;
    }

    /**
     * 生成员工 Token
     * @param id 员工id
     * @return token字符串
     */
    @Override
    public String buildEmpToken(Long id) {
        // 登录成功后，生成jwt令牌
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(Constants.JwtClaimsConstant.EMP_ID, id);
        String token = createJWT(
                jwtConfigProperties.getAdminSecretKey(),
                jwtConfigProperties.getAdminTtl(),
                claims);

        return token;
    }

}
