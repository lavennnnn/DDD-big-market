package cn.hush.test.infrastructure.persistent;

import cn.hush.infrastructure.persistent.dao.IRaffleActivityDao;
import cn.hush.infrastructure.persistent.po.RaffleActivityPO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author Hush
 * @description 抽奖活动配置Dao测试
 * @create 2024-11-14 下午10:59
 */

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class RaffleActivityDaoTest {

    @Resource
    private IRaffleActivityDao raffleActivityDao;

    @Test
    public void test_queryRaffleActivityByActivityId() {
        RaffleActivityPO raffleActivity = raffleActivityDao.queryRaffleActivityByActivityId(100301L);
        log.info("测试结果：{}", JSON.toJSONString(raffleActivity));


    }
}