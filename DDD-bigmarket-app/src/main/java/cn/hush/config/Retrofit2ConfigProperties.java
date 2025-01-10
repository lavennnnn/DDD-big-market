package cn.hush.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Hush
 * @description
 * @create 2025-01-10 下午1:36
 */
@Data
@ConfigurationProperties(prefix = "gateway.config", ignoreInvalidFields = true)
public class Retrofit2ConfigProperties {

    /** 状态；open = 开启、close 关闭 */
    private boolean enable;
    /** 转发地址 */
    private String apiHost;

}
