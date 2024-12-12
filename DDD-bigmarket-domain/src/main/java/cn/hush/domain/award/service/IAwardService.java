package cn.hush.domain.award.service;

import cn.hush.domain.award.model.entity.DistributeAwardEntity;
import cn.hush.domain.award.model.entity.UserAwardRecordEntity;

/**
 * @author Hush
 * @description 奖品服务接口
 * @create 2024-11-27 下午10:19
 */
public interface IAwardService {

    void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity);

    //配送奖品
    void distributeAward(DistributeAwardEntity distributeAwardEntity);
}
