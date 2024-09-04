package cn.hush.domain.strategy.service.armory;

/**
 * @author Hush
 * @description 策略装配库（兵工厂），负责初始化策略计算
 * @create 2024-08-28 下午8:43
 */

public interface IStrategyArmory {

    /**
     * 装配抽奖策略配置
     *
     * @param strategyId
     * @return 装配结果
     */
    boolean assembleLotteryStrategy(Long strategyId);


}
