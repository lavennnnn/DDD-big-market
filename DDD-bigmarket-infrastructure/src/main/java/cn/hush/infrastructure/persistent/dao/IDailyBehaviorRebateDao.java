package cn.hush.infrastructure.persistent.dao;

import cn.hush.domain.rebate.model.valobj.BehaviorTypeVO;
import cn.hush.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import cn.hush.infrastructure.persistent.po.DailyBehaviorRebatePO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Hush
 * @description 日常行为返利活动配置
 * @create 2024-12-03 下午7:15
 */

@Mapper
public interface IDailyBehaviorRebateDao {

    List<DailyBehaviorRebatePO> queryDailyBehaviorRebateConfigByBehaviorType(String behaviorType);

}
