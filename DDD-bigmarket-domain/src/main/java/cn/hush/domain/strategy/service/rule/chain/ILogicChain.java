package cn.hush.domain.strategy.service.rule.chain;

/**
 * @author Hush
 * @description 责任链接口
 * @create 2024-10-14 下午8:11
 */

public interface ILogicChain extends ILogicChainArmory {

    /**
     * 责任链接口
     *
     * @param userId     用户ID
     * @param strategyId 策略ID
     * @return 奖品ID
     */
    Integer logic(String userId, Long strategyId);


}
