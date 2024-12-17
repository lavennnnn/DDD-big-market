package cn.hush.infrastructure.persistent.dao;
import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import cn.hush.infrastructure.persistent.po.RaffleActivityAccountPO;
import cn.hush.infrastructure.persistent.po.RaffleActivityCountPO;
import cn.hush.infrastructure.persistent.po.RaffleActivityOrderPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Hush
 * @description 抽奖活动账户表
 * @create 2024-11-14 下午10:47
 */

@Mapper

public interface IRaffleActivityAccountDao {
    //这里的其他方法不添加 @DBRouter ，因为在实现的事务手动进行了路由处理

    int updateAccountQuota(RaffleActivityAccountPO raffleActivityAccount);

    void insert(RaffleActivityAccountPO raffleActivityAccount);

    @DBRouter
    RaffleActivityAccountPO queryActivityAccountByUserId(RaffleActivityAccountPO raffleActivityAccountReq);

    int updateActivityAccountSubtractionQuota(RaffleActivityAccountPO raffleActivityAccount);

    void updateActivityAccountMonthSurplusImageQuota(RaffleActivityAccountPO raffleActivityAccount);

    void updateActivityAccountDaySurplusImageQuota(RaffleActivityAccountPO raffleActivityAccount);
}
