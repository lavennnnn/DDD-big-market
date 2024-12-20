package cn.hush.config;

import cn.hush.types.annotations.DCCValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hush
 * @description Dynamic Config Center bean factory
 *                      基于 Zookeeper 的配置中心实现原理
 * @create 2024-12-21 上午1:54
 */
@Configuration
@Slf4j
public class DCCValueBeanFactory implements BeanPostProcessor {

    private static final String BASE_CONFIG_PATH = "/big-market-dcc";
    private static final String BASE_CONFIG_PATH_CONFIG = BASE_CONFIG_PATH + "/config";

    private final CuratorFramework client;

    private final Map<String, Object> dccObjGroup = new HashMap<>();

    public DCCValueBeanFactory(CuratorFramework client) throws Exception {
        this.client = client;

        //节点判断
        if (null == client.checkExists().forPath(BASE_CONFIG_PATH_CONFIG)) {
            client.create().creatingParentsIfNeeded().forPath(BASE_CONFIG_PATH_CONFIG);
            log.info("DCC 节点监听 base node {} not absent create new done!", BASE_CONFIG_PATH_CONFIG);
        }

        CuratorCache curatorCache = CuratorCache.build(client, BASE_CONFIG_PATH_CONFIG);
        curatorCache.start();

        curatorCache.listenable().addListener((type, oldData, newData)->{
            switch (type) {
                case NODE_CHANGED:
                    String dccValuePath = newData.getPath();
                    Object objectBean = dccObjGroup.get(dccValuePath);
                    if (null == objectBean) return;

                    try {
                        // 1. getDeclaredField 方法用于获取指定类中声明的所有字段，包括私有字段、受保护字段和公共字段。
                        // 2. getField 方法用于获取指定类中的公共字段，即只能获取到公共访问修饰符（public）的字段。
                        Field field = objectBean.getClass().getDeclaredField(dccValuePath.substring(dccValuePath.lastIndexOf("/") + 1));
                        field.setAccessible(true);
                        field.set(objectBean, new String(newData.getData()));
                        field.setAccessible(false);
                    }catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Class<?> beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(DCCValue.class)) {
                DCCValue dccValue = field.getAnnotation(DCCValue.class);

                String value = dccValue.value();
                if (StringUtils.isBlank(value)) {
                    throw new RuntimeException("dccValue is not defined ");
                }

                String[] split = value.split(":");
                String key = split[0];
                String defaultValue = split.length == 2 ? split[1] : null;

                String keyPath = BASE_CONFIG_PATH.concat("/").concat(key);
                try {
                    // 判断当前节点是否存在，不存在则创建出 Zookeeper 节点
                    if (null == client.checkExists().forPath(keyPath)){
                        client.create().creatingParentsIfNeeded().forPath(keyPath);
                        if (StringUtils.isNotBlank(defaultValue)) {
                            field.setAccessible(true);
                            field.set(bean, defaultValue);
                            field.setAccessible(false);
                        }
                        log.info("DCC 节点监听 创建节点 {}", keyPath);
                    } else {
                        String configValue = new String(client.getData().forPath(keyPath));
                        if (StringUtils.isNotBlank(configValue)) {
                            field.setAccessible(true);
                            field.set(bean, configValue);
                            field.setAccessible(false);
                            log.info("DCC 节点监听 设置配置 {} {} {}", keyPath, field.getName(), configValue);
                        }
                    }
                }catch (Exception e) {
                    throw new RuntimeException(e);
                }
                dccObjGroup.put(BASE_CONFIG_PATH_CONFIG.concat("/").concat(key), bean);
            }
        }

        return bean;
    }
}












