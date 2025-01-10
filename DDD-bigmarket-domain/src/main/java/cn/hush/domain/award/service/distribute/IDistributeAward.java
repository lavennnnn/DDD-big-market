package cn.hush.domain.award.service.distribute;

import cn.hush.domain.award.model.entity.DistributeAwardEntity;

/**
 * @author Hush
 * @description 分发奖品接口
 * @create 2024-12-12 上午11:25
 */

public interface IDistributeAward {

    void giveOutPrizes(DistributeAwardEntity distributeAwardEntity) throws Exception;

}
