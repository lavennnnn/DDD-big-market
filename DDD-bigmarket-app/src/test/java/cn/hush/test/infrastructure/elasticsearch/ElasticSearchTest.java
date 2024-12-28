package cn.hush.test.infrastructure.elasticsearch;

import cn.hush.infrastructure.elasticsearch.IElasticSearchUserRaffleOrderDao;
import cn.hush.infrastructure.elasticsearch.po.UserRaffleOrderPO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Hush
 * @description
 * @create 2024-12-28 下午9:36
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticSearchTest {

    @Resource
    private IElasticSearchUserRaffleOrderDao elasticSearchUserRaffleOrderDao;

    @Test
    public void test_queryUserRaffleOrderList() {
        List<UserRaffleOrderPO> userRaffleOrders = elasticSearchUserRaffleOrderDao.queryUserRaffleOrderList();
        log.info("测试结果：{}", JSON.toJSONString(userRaffleOrders));
    }

}
