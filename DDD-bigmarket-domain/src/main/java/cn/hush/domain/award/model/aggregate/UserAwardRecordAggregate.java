package cn.hush.domain.award.model.aggregate;

import cn.hush.domain.award.model.entity.TaskEntity;
import cn.hush.domain.award.model.entity.UserAwardRecordEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Hush
 * @description 用户中奖记录聚合对象 【聚合代表一个事务操作】
 * @create 2024-11-27 下午11:14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAwardRecordAggregate {

    private UserAwardRecordEntity userAwardRecordEntity;

    private TaskEntity taskEntity;

}
