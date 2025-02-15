package cn.hush.infrastructure.dao;

import cn.hush.infrastructure.dao.po.StrategyAwardPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 策略奖品明细配置 - 概率，规则
 */

@Mapper
public interface IStrategyAwardDao {
    
    List<StrategyAwardPO> queryStrategyAwardList();

    List<StrategyAwardPO> queryStrategyAwardListByStrategyId(Long strategyId);

    String queryStrategyRuleModelValue(StrategyAwardPO strategyAwardPO);

    void updateStrategyAwardStock(StrategyAwardPO strategyAwardPO);

    StrategyAwardPO queryStrategyAward(StrategyAwardPO strategyAwardReq);

    List<StrategyAwardPO> queryOpenActivityStrategyAwardList();
}
