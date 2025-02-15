package cn.hush.domain.award.adapter.repository;

import cn.hush.domain.award.model.aggregate.GiveOutPrizesAggregate;
import cn.hush.domain.award.model.aggregate.UserAwardRecordAggregate;
import cn.hush.domain.award.model.entity.UserAwardRecordEntity;

/**
 * @author Hush
 * @description 奖品仓储服务
 * @create 2024-11-28 上午12:43
 */
public interface IAwardRepository {

    void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate);

    String queryAwardConfig(Integer awardId);

    void saveGiveOutPrizesAggregate(GiveOutPrizesAggregate giveOutPrizesAggregate);

    String queryAwardKey(Integer awardId);
}
