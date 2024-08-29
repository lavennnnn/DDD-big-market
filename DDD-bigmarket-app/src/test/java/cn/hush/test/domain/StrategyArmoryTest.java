package cn.hush.test.domain;

import cn.hush.domain.strategy.service.armory.IStrategyArmory;
import cn.hush.infrastructure.redis.IRedisService;
import cn.hush.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author Hush
 * @description
 * @create 2024-08-29 上午1:30
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyArmoryTest {

    @Resource
    private IStrategyArmory strategyArmory;

    @Resource
    private IRedisService redisService;

    @Test
    public void test_strategyArmory() {
        strategyArmory.assembleLotteryStrategy(100002L);
    }

    @Test
    public void test_getAssembleRandomVal() {
        log.info("测试结果：{} - 奖品ID值", strategyArmory.getRandomAward(10002L));
        log.info("测试结果：{} - 奖品ID值", strategyArmory.getRandomAward(10002L));
        log.info("测试结果：{} - 奖品ID值", strategyArmory.getRandomAward(10002L));
    }


}
