package cn.hush.domain.activity.service.quota.rule;

/**
 * @author Hush
 * @description 规则装配接口
 * @create 2024-11-19 下午8:44
 */
public interface IActionChainArmory {

    IActionChain next();

    IActionChain appendNext(IActionChain next);

}
