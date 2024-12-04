package cn.hush.domain.rebate.repository;

import cn.hush.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import cn.hush.domain.rebate.model.entity.BehaviorEntity;
import cn.hush.domain.rebate.model.valobj.BehaviorTypeVO;
import cn.hush.domain.rebate.model.valobj.DailyBehaviorRebateVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hush
 * @description 行为返利服务仓储接口
 * @create 2024-12-04 下午9:56
 */

public interface IBehaviorRebateRepository {

    //查询日常行为返利配置
    List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorTypeVO);

    //保存聚合
    void saveUserRebateRecord(String userId, ArrayList<BehaviorRebateAggregate> behaviorRebateAggregateList);
}
