package cn.hush.infrastructure.persistent.dao;
import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import cn.hush.infrastructure.persistent.po.RaffleActivityAccountPO;
import cn.hush.infrastructure.persistent.po.UserRaffleOrderPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Hush
 * @description 用户抽奖订单表
 * @create 2024-11-14 下午10:47
 */

@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserRaffleOrderDao {

    void insert(UserRaffleOrderPO userRaffleOrderPO);

    @DBRouter
    UserRaffleOrderPO queryNoUsedRaffleOrder(UserRaffleOrderPO userRaffleOrderReq);

    int updateUserRaffleOrderStateUsed(UserRaffleOrderPO userRaffleOrderReq);
}
