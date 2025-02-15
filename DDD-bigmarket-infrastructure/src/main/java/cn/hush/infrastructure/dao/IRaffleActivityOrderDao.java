package cn.hush.infrastructure.dao;
import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import cn.hush.infrastructure.dao.po.RaffleActivityOrderPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Hush
 * @description 抽奖活动单Dao
 * @create 2024-11-14 下午10:47
 */

@Mapper
@DBRouterStrategy(splitTable = true)
public interface IRaffleActivityOrderDao {

    @DBRouter(key = "userId")
    void insert(RaffleActivityOrderPO raffleActivityOrder);

    @DBRouter
    List<RaffleActivityOrderPO> queryRaffleActivityOrderByUserId(String userId);

    @DBRouter
    RaffleActivityOrderPO queryRaffleActivityOrder(RaffleActivityOrderPO raffleActivityOrderReq);

    int updateOrderCompleted(RaffleActivityOrderPO raffleActivityOrderReq);

    @DBRouter
    RaffleActivityOrderPO queryUnpaidActivityOrder(RaffleActivityOrderPO raffleActivityOrderReq);
}
