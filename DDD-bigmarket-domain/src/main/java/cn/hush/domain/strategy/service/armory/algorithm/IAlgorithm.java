package cn.hush.domain.strategy.service.armory.algorithm;

import cn.hush.domain.strategy.model.entity.StrategyAwardEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Hush
 * @description
 * @create 2024-12-31 下午2:04
 */

public interface IAlgorithm {

    void armoryAlgorithm(String key, List<StrategyAwardEntity> strategyAwardEntities,
                         BigDecimal rateRange);

    Integer dispatchAlgorithm(String key);


}
