package cn.hush.domain.strategy.repository;

import cn.hush.domain.strategy.model.entity.StrategyAwardEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * @author Hush
 * @description 策略仓储接口
 * @create 2024-08-28 下午8:52
 */
public interface IStrategyRepository {

    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchRateTables(Long strategyId, BigDecimal rateRange, HashMap<Integer, Integer> shuffledStrategyAwardSearchRateTables);

    BigDecimal getRateRange(Long strategyId);

    Integer getStrategyAwardAssemble(Long strategyId, int rateKey);
}
