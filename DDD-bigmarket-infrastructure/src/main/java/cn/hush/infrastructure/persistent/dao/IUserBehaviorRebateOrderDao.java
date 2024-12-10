package cn.hush.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import cn.hush.infrastructure.persistent.po.UserBehaviorRebateOrderPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Hush
 * @description 用户行为返利流水
 * @create 2024-12-03 下午7:15
 */

@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserBehaviorRebateOrderDao {

    void insert(UserBehaviorRebateOrderPO userBehaviorRebateOrder);

    @DBRouter
    List<UserBehaviorRebateOrderPO> queryOrderByOutBusinessNo(UserBehaviorRebateOrderPO userBehaviorRebateOrderPO);
}
