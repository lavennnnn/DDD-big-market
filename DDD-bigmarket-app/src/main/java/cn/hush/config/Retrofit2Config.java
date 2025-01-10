package cn.hush.config;

import cn.hush.infrastructure.gateway.IOpenAIAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * @author Hush
 * @description
 * @create 2025-01-10 下午1:35
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({Retrofit2ConfigProperties.class})
public class Retrofit2Config {

    @Bean
    public Retrofit retrofit(Retrofit2ConfigProperties properties) {
        return new Retrofit.Builder()
                .baseUrl(properties.getApiHost())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Bean
    public IOpenAIAccountService weixinApiService(Retrofit retrofit) {
        return retrofit.create(IOpenAIAccountService.class);
    }



}
