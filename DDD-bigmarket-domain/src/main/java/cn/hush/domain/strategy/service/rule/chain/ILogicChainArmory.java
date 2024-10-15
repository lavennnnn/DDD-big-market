package cn.hush.domain.strategy.service.rule.chain;

/**
 * @author Hush
 * @description 装配接口
 * @create 2024-10-15 下午6:53
 */
public interface ILogicChainArmory {

    ILogicChain appendNext(ILogicChain next);

    ILogicChain next();

}
