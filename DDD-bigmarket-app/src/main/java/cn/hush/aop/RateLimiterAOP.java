package cn.hush.aop;

import cn.hush.types.annotations.DCCValue;
import cn.hush.types.annotations.RateLimiterAccessInterceptor;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author Hush
 * @description
 * @create 2024-12-25 下午12:28
 */
@Slf4j
@Aspect
@Component
public class RateLimiterAOP {

    @DCCValue("rateLimiterSwitch:close")
    private String rateLimiterSwitch;

    // 个人限频记录1分钟
    private final Cache<String, RateLimiter> loginRecord = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    // 个人限频黑名单24h - 分布式业务场景，可以记录到 Redis 中
    private final Cache<String, Long> blackList = CacheBuilder.newBuilder()
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build();

    @Pointcut("@annotation(cn.hush.types.annotations.RateLimiterAccessInterceptor)")
    public void aopPoint(){
    }

    @Around("aopPoint() && @annotation(rateLimiterAccessInterceptor)")
    public Object rateLimit(ProceedingJoinPoint pjp, RateLimiterAccessInterceptor rateLimiterAccessInterceptor) throws Throwable {

        // 0. 限流开关【open 开启、close 关闭】关闭后，不会走限流策略
        if (StringUtils.isBlank(rateLimiterSwitch) || "close".equals(rateLimiterSwitch)) {
            return pjp.proceed();
        }

        String key = rateLimiterAccessInterceptor.key();
        if (StringUtils.isBlank(key)) {
            throw new RuntimeException();
        }

        // 获取拦截字段
        String keyAttr = getAttrValue(key, pjp.getArgs());
        log.info("aop attr: {}",keyAttr);

        // 获取限流 -> Guava 缓存1分钟
        if (!"all".equals(keyAttr) &&
                rateLimiterAccessInterceptor.blacklistCount() != 0 &&
                null != blackList.getIfPresent(keyAttr) &&
                blackList.getIfPresent(keyAttr) > rateLimiterAccessInterceptor.blacklistCount()) {

            log.info("限流-超频次拦截: {}",keyAttr);
            return fallbackMethodResult(pjp, rateLimiterAccessInterceptor.fallbackMethod());

        }

        // 获取限流 -> Guava 缓存1分钟
        RateLimiter rateLimiter = loginRecord.getIfPresent(keyAttr);
        if (null ==  rateLimiter) {
            rateLimiter = RateLimiter.create(rateLimiterAccessInterceptor.permitsPerSecond());
            loginRecord.put(keyAttr, rateLimiter);
        }

        // 限流拦截
        if(!rateLimiter.tryAcquire()) {
            if (rateLimiterAccessInterceptor.blacklistCount() != 0) {
                if (null == blackList.getIfPresent(keyAttr)) {
                    blackList.put(keyAttr, 1L);
                }else {
                    blackList.put(keyAttr, blackList.getIfPresent(keyAttr) + 1);
                }
            }
            log.info("限流-超频次拦截: {}",keyAttr);
            return fallbackMethodResult(pjp, rateLimiterAccessInterceptor.fallbackMethod());
        }

        return pjp.proceed();
    }

    /**
     * 调用用户配置的回调方法，当拦截后，返回回调结果。
     */
    private Object fallbackMethodResult(JoinPoint jp, String fallbackMethod)throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Signature signature = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = jp.getTarget().getClass().getMethod(fallbackMethod, methodSignature.getParameterTypes());
        return method.invoke(jp.getThis(), jp.getArgs());
    }

    /**
     * 实际根据自身业务调整，主要是为了获取通过某个值做拦截
     */
    public String getAttrValue(String attr, Object[] args) {
        if (args[0] instanceof String) {
            return args[0].toString();
        }
        String filedValue = null;
        for (Object arg : args) {
            try {
                if (StringUtils.isNotBlank(filedValue)) {
                    break;
                }
                // filedValue = BeanUtils.getProperty(arg, attr);
                // fix: 使用lombok时，uId这种字段的get方法与idea生成的get方法不同，会导致获取不到属性值，改成反射获取解决
                filedValue = String.valueOf(this.getValueByName(args, attr));
            }catch (Exception e) {
                log.error("获取属性值失败 attr:{}",attr,e);
            }
        }
        return filedValue;
    }

    /**
     * 获取对象的特定属性值
     *
     * @param item 对象
     * @param name 属性名
     * @return 属性值
     * @author tang
     */
    private Object getValueByName (Object item, String name) {
        try {
            Field field = getFieldByName(item, name);
            if (field == null) {
                return null;
            }
            field.setAccessible(true);
            Object o = field.get(item);
            field.setAccessible(false);
            return o;
        }catch (IllegalAccessException e) {
            return null;
        }
    }


    /**
     * 根据名称获取方法，该方法同时兼顾继承类获取父类的属性
     *
     * @param item 对象
     * @param name 属性名
     * @return 该属性对应方法
     * @author tang
     */
    private Field getFieldByName(Object item, String name) {
        try {
            Field field;
            try {
                field = item.getClass().getDeclaredField(name);
            }catch (NoSuchFieldException e) {
                field = item.getClass().getSuperclass().getDeclaredField(name);
            }
            return field;
        }catch (NoSuchFieldException e) {
            return null;
        }
    }

}
