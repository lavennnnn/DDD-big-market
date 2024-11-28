package cn.hush.infrastructure.persistent.dao;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import cn.hush.infrastructure.persistent.po.RaffleActivityAccountPO;
import cn.hush.infrastructure.persistent.po.UserAwardRecordPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Hush
 * @description 用户中奖记录表
 * @create 2024-11-14 下午10:47
 */

@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserAwardRecordDao {


    void insert(UserAwardRecordPO userAwardRecord);
}
