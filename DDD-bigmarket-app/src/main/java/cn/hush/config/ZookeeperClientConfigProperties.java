package cn.hush.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Hush
 * @description zookeeper客户端配置属性类
 * @create 2024-12-20 下午7:41
 */
@Data
@ConfigurationProperties(prefix = "zookeeper.sdk.config", ignoreInvalidFields = true)
public class ZookeeperClientConfigProperties {

    private String connectString;
    private int baseSleepTimeMs;
    private int maxRetries;
    private int sessionTimeoutMs;
    private int connectionTimeoutMs;

}
