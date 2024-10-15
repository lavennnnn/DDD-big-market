package cn.hush.domain.strategy.service.rule.chain;

/**
 * @author Hush
 * @description
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
