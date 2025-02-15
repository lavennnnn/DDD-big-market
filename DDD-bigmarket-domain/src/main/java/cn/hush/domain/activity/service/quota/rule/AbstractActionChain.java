package cn.hush.domain.activity.service.quota.rule;

/**
 * @author Hush
 * @description 下单规则责任链抽象类
 * @create 2024-11-19 下午8:39
 */

public abstract class AbstractActionChain implements IActionChain {


    private IActionChain next;

    @Override
    public IActionChain next() {
        return next;
    }

    @Override
    public IActionChain appendNext(IActionChain next) {
        this.next = next;
        return next;
    }

}
