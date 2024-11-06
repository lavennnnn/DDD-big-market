package cn.hush.domain.strategy.service.rule.chain;

/**
 * @author Hush
 * @description 抽奖策略责任链，判断走哪种抽奖策略。如；默认抽象、权重抽奖、黑名单抽奖
 * @create 2024-10-14 下午8:14
 */

public abstract class AbstractLogicChain implements ILogicChain {

    private ILogicChain next;

    @Override
    public ILogicChain appendNext(ILogicChain next) {
        this.next = next;
        return this;
    }

    @Override
    public ILogicChain next() {
        return next;
    }

    protected abstract String ruleModel();

}
