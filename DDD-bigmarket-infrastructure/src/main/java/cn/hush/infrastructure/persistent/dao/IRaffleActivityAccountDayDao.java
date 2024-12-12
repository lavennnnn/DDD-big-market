package cn.hush.infrastructure.persistent.dao;
import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.hush.infrastructure.persistent.po.RaffleActivityAccountDayPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Hush
 * @description 抽奖活动账户表-日次数
 * @create 2024-11-14 下午10:47
 */

@Mapper
public interface IRaffleActivityAccountDayDao {

    //这里的其他方法不添加 @DBRouter ，因为在实现的事务手动进行了路由处理

    @DBRouter
    RaffleActivityAccountDayPO queryActivityAccountDayByUserId(RaffleActivityAccountDayPO raffleActivityAccountDayPO);

    int updateActivityAccountDaySubtractionQuota(RaffleActivityAccountDayPO raffleActivityAccountDayPO);

    void insertActivityAccountDay(RaffleActivityAccountDayPO build);

    @DBRouter
    Integer queryRaffleActivityAccountDayPartakeCount(RaffleActivityAccountDayPO raffleActivityAccountDay);

    void addAccountQuota(RaffleActivityAccountDayPO raffleActivityAccountDay);
}