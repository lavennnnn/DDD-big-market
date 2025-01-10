package cn.hush.infrastructure.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Hush
 * @description
 * @create 2025-01-07 上午4:40
 */
@Component
@ConfigurationProperties(prefix = "erp.jwt")
@Data
public class JwtConfigProperties {

    /**
     * 管理端员工生成jwt令牌相关配置
     */
    private String adminSecretKey;
    private long adminTtl;
    private String adminTokenName;

    /**
     * 用户端用户生成jwt令牌相关配置
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;


}
