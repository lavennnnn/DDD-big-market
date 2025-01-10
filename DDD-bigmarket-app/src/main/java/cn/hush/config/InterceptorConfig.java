package cn.hush.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;

/**
 * @author Hush
 * @description
 * @create 2025-01-07 上午5:46
 */
@Configuration
@Slf4j
public class InterceptorConfig extends WebMvcConfigurationSupport {

    @Resource
    private JwtEmployeeInterceptor jwtEmployeeInterceptor;

    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        //员工拦截器
        registry.addInterceptor(jwtEmployeeInterceptor)
                .addPathPatterns("/api/${app.config.api-version}/raffle/erp/**")
                .excludePathPatterns("/api/${app.config.api-version}/raffle/erp/login");
    }

}
