package cn.hush;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Configurable
@EnableScheduling
@EnableDubbo
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableHystrix
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class);
    }

}
