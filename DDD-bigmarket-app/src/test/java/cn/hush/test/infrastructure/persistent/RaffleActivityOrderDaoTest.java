package cn.hush.test.infrastructure.persistent;

import cn.hush.infrastructure.persistent.dao.IRaffleActivityOrderDao;
import cn.hush.infrastructure.persistent.po.RaffleActivityOrderPO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.jeasy.random.EasyRandom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Hush
 * @description 抽奖活动订单测试
 * @create 2024-11-14 下午10:59
 */

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class RaffleActivityOrderDaoTest {

    @Resource
    private IRaffleActivityOrderDao raffleActivityOrderDao;

    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    public void test_insert_random() {
        for (int i = 0; i < 50; i++) {
            RaffleActivityOrderPO raffleActivityOrder = new RaffleActivityOrderPO();
            // EasyRandom 可以通过指定对象类的方式，随机生成对象值。如；easyRandom.nextObject(String.class)、easyRandom.nextObject(RaffleActivityOrder.class)
            raffleActivityOrder.setUserId(easyRandom.nextObject(String.class));
            raffleActivityOrder.setActivityId(100301L);
            raffleActivityOrder.setActivityName("测试活动");
            raffleActivityOrder.setStrategyId(100006L);
            raffleActivityOrder.setOrderId(RandomStringUtils.randomNumeric(12));
            raffleActivityOrder.setOrderTime(new Date());
            raffleActivityOrder.setState("not_used");
            // 插入数据
            raffleActivityOrderDao.insert(raffleActivityOrder);
        }
    }

    @Test
    public void test_insert() {
        RaffleActivityOrderPO raffleActivityOrder = new RaffleActivityOrderPO();
        raffleActivityOrder.setUserId("xiaofuge");
        raffleActivityOrder.setActivityId(100301L);
        raffleActivityOrder.setActivityName("测试活动");
        raffleActivityOrder.setStrategyId(100006L);
        raffleActivityOrder.setOrderId(RandomStringUtils.randomNumeric(12));
        raffleActivityOrder.setOrderTime(new Date());
        raffleActivityOrder.setState("not_used");
        // 插入数据
        raffleActivityOrderDao.insert(raffleActivityOrder);
    }

    @Test
    public void test_queryRaffleActivityOrderByUserId() {
        String userId = "xiaofuge";
        List<RaffleActivityOrderPO> raffleActivityOrders = raffleActivityOrderDao.queryRaffleActivityOrderByUserId(userId);
        log.info("测试结果：{}", JSON.toJSONString(raffleActivityOrders));
    }

}