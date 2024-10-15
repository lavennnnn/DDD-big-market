package cn.hush.domain.strategy.service.rule.filter.factory;

import cn.hush.domain.strategy.model.entity.RuleActionEntity;
import cn.hush.domain.strategy.service.annotation.LogicStrategy;
import cn.hush.domain.strategy.service.rule.filter.ILogicFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Hush
 * @description 规则工厂
 * @create 2024-10-10 上午12:35
 */
@Service
public class DefaultLogicFactory {

    public Map<String, ILogicFilter<?>> logicFilterMap = new ConcurrentHashMap<>();

    public DefaultLogicFactory(List<ILogicFilter<?>> logicFilters) {
        //todo need to be comprehend
        logicFilters.forEach(logic -> {
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logic.getClass(), LogicStrategy.class);
            if (null != strategy) {
                logicFilterMap.put(strategy.logicModel().getCode(), logic);
            }
        });

    }

    public <T extends RuleActionEntity.RaffleEntity> Map<String, ILogicFilter<T>> openLogicFilter() {
        return (Map<String, ILogicFilter<T>>) (Map<?, ?>) logicFilterMap;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {

        RULE_WIGHT("rule_weight","【抽奖前规则】根据抽奖权重返回可抽奖范围KEY","before"),
        RULE_BLACKLIST("rule_blacklist","【抽奖前规则】黑名单规则过滤，命中黑名单则直接返回","before"),
        RULE_LOCK("rule_lock","【抽奖中规则】抽奖N次后，对应奖品可解锁抽奖权限","during"),
        RULE_LUCK_AWARD("rule_luck","【抽奖后规则】兜底幸运奖","after")

        ;

        private final String code;
        private final String info;
        private final String type;

        //抽奖中
        public static boolean isDuring(String code){
            return "during".equals(LogicModel.valueOf(code.toUpperCase()).type);
        }

        //抽奖后
        public static boolean isAfter(String code){
            return "after".equals(LogicModel.valueOf(code.toUpperCase()).type);
        }

    }




}










