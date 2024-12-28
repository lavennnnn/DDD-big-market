package cn.hush.infrastructure.dao;
import cn.hush.infrastructure.dao.po.RaffleActivityCountPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Hush
 * @description 抽奖活动次数配置表Dao
 * @create 2024-11-14 下午10:47
 */

@Mapper
public interface IRaffleActivityCountDao {

    RaffleActivityCountPO queryRaffleActivityCountByActivityCountId(Long activityCountId);

}
