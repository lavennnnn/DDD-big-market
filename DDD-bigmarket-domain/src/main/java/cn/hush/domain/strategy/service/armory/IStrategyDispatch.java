package cn.hush.domain.strategy.service.armory;

import java.util.Date;

/**
 * @author Hush
 * @description 策略抽奖调度
 * @create 2024-09-02 下午5:15
 */
public interface IStrategyDispatch {

    /**
     * 获取抽奖策略装配的随机结果
     * @param strategyId 策略id
     * @return 抽奖结果
     */
    Integer getRandomAwardId(Long strategyId);

    Integer getRandomAwardId(Long strategyId, String ruleWeightValue);

    Integer getRandomAwardId(String key);
    /**
     * 根据策略id和奖品id,扣减奖品缓存库存
     * @param strategyId 策略id
     * @param awardId 奖品id
     * @param endDateTime 结束时间
     * @return 扣减结果
     */
    Boolean subtractAwardStock (Long strategyId, Integer awardId, Date endDateTime);
}
