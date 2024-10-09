package cn.hush.domain.strategy.service;

import cn.hush.domain.strategy.model.entity.RaffleAwardEntity;
import cn.hush.domain.strategy.model.entity.RaffleFactorEntity;

/**
 * @author Hush
 * @description 抽奖策略接口
 * @create 2024-10-09 下午9:51
 */
public interface IRaffleStrategy {

    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactor);

}
