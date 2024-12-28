package cn.hush.infrastructure.dao;

import cn.hush.infrastructure.dao.po.StrategyPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 抽奖策略
 *
 */

@Mapper
public interface IStrategyDao {

    List<StrategyPO> queryStrategyList();

    StrategyPO queryStrategyByStrategyId(Long strategyId);
}
