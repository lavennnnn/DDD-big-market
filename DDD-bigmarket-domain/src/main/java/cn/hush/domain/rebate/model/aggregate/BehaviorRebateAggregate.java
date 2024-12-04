package cn.hush.domain.rebate.model.aggregate;

import cn.hush.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import cn.hush.domain.rebate.model.entity.TaskEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Hush
 * @description 行为返利聚合对象
 * @create 2024-12-04 下午10:11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BehaviorRebateAggregate {

    private String userId;

    private BehaviorRebateOrderEntity behaviorRebateOrderEntity;

    private TaskEntity taskEntity;

}
