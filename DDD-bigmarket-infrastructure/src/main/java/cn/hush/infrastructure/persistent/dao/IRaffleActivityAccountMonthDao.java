package cn.hush.infrastructure.persistent.dao;
import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.hush.infrastructure.persistent.po.RaffleActivityAccountMonthPO;
import cn.hush.infrastructure.persistent.po.RaffleActivityAccountPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Hush
 * @description 抽奖活动账户表 - 月次数
 * @create 2024-11-14 下午10:47
 */

@Mapper

public interface IRaffleActivityAccountMonthDao {

    //这里的其他方法不添加 @DBRouter ，因为在实现的事务手动进行了路由处理

    @DBRouter
    RaffleActivityAccountMonthPO queryActivityAccountMonthByUserId(RaffleActivityAccountMonthPO raffleActivityAccountMonthPO);

    int updateActivityAccountMonthSubtractionQuota(RaffleActivityAccountMonthPO raffleActivityAccountMonthPO);

    void insertActivityAccountMonth(RaffleActivityAccountMonthPO build);
}
