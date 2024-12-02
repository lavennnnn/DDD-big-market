package cn.hush.domain.strategy.service;

import java.util.Map;

/**
 * @author Hush
 * @description 抽奖规则接口
 * @create 2024-12-01 上午10:25
 */
public interface IRaffleRule {

    //查询解锁次数
    Map<String, Integer> queryAwardRuleLockCount(String[] treeIds);

}
