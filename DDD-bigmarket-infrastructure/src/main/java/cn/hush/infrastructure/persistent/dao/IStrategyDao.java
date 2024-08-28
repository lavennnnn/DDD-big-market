package cn.hush.infrastructure.persistent.dao;

import cn.hush.infrastructure.persistent.po.StrategyAwardPO;
import cn.hush.infrastructure.persistent.po.StrategyPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 抽奖策略
 *
 */

@Mapper
public interface IStrategyDao {

    List<StrategyPO> queryStrategyList();

}
