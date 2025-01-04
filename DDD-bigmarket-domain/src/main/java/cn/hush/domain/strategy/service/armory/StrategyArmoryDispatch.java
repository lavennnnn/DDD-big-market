package cn.hush.domain.strategy.service.armory;

import cn.hush.domain.strategy.model.entity.StrategyAwardEntity;
import cn.hush.domain.strategy.model.entity.StrategyEntity;
import cn.hush.domain.strategy.model.entity.StrategyRuleEntity;
import cn.hush.domain.strategy.repository.IStrategyRepository;
import cn.hush.domain.strategy.service.armory.algorithm.AbstractAlgorithm;
import cn.hush.domain.strategy.service.armory.algorithm.IAlgorithm;
import cn.hush.types.common.Constants;
import cn.hush.types.enums.ResponseCode;
import cn.hush.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;


/**
 * @author Hush
 * @description 策略装配（兵工厂），负责初始化策略计算
 * @create 2024-08-28 下午8:51
 */

@Slf4j
@Service
public class StrategyArmoryDispatch extends AbstractStrategyArmoryDispatch{

    private final Map<String, IAlgorithm> algorithmMap;

    public StrategyArmoryDispatch(Map<String, IAlgorithm> algorithmMap) {
        this.algorithmMap = algorithmMap;
    }

    private final Integer ALGORITHM_THRESHOLD_VALUE = 10000;

    @Override
    protected void armoryAlgorithm(String key, List<StrategyAwardEntity> strategyAwardEntities) {
        BigDecimal minAwardRate = minAwardRate(strategyAwardEntities);
        double rateRange = convert(minAwardRate.doubleValue());

        if (rateRange <= ALGORITHM_THRESHOLD_VALUE) {
            // 用实现类的 bean 对象名字命名 (o1Algorithm)
            IAlgorithm o1Algorithm = algorithmMap.get(AbstractAlgorithm.Algorithm.O1.getKey());
            o1Algorithm.armoryAlgorithm(key, strategyAwardEntities, new BigDecimal(rateRange));
            repository.cacheStrategyArmoryAlgorithm(key, AbstractAlgorithm.Algorithm.O1.getKey());
        } else {
            // 用实现类的 bean 对象名字命名 (o1Algorithm)
            IAlgorithm oLogNAlgorithm = algorithmMap.get(AbstractAlgorithm.Algorithm.OLogN.getKey());
            oLogNAlgorithm.armoryAlgorithm(key, strategyAwardEntities, new BigDecimal(rateRange));
            repository.cacheStrategyArmoryAlgorithm(key, AbstractAlgorithm.Algorithm.O1.getKey());
        }
    }

    @Override
    protected Integer dispatchAlgorithm(String key) {

        String beanName = repository.queryStrategyArmoryAlgorithmFromCache(key);
        IAlgorithm algorithm = algorithmMap.get(beanName);

        return algorithm.dispatchAlgorithm(key);

    }
}







