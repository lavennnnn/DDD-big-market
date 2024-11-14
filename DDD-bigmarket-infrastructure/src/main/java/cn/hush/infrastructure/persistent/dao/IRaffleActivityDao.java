package cn.hush.infrastructure.persistent.dao;
import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import cn.hush.infrastructure.persistent.po.RaffleActivityOrderPO;
import cn.hush.infrastructure.persistent.po.RaffleActivityPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Hush
 * @description 抽奖活动表Dao
 * @create 2024-11-14 下午10:47
 */

@Mapper
public interface IRaffleActivityDao {

    RaffleActivityPO queryRaffleActivityByActivityId(Long activityId);

}
