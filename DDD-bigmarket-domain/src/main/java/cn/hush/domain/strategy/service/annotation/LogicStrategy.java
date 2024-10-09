package cn.hush.domain.strategy.service.annotation;

import cn.hush.domain.strategy.service.rule.factory.DefaultLogicFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Hush
 * @description 策略自定义枚举
 * @create 2024-10-09 下午11:48
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogicStrategy {

    DefaultLogicFactory.LogicModel logicModel();

}
