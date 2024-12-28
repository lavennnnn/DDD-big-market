package cn.hush.infrastructure.dao;
import cn.hush.infrastructure.dao.po.RaffleActivityPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Hush
 * @description 抽奖活动表Dao
 * @create 2024-11-14 下午10:47
 */

@Mapper
public interface IRaffleActivityDao {

    RaffleActivityPO queryRaffleActivityByActivityId(Long activityId);

    Long queryStrategyIdByActivityId(Long activityId);

    Long queryActivityIdByStrategyId(Long strategyId);

}
