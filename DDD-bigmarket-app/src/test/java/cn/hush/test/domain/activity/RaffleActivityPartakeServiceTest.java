package cn.hush.test.domain.activity;

import cn.hush.domain.activity.model.entity.PartakeRaffleActivityEntity;
import cn.hush.domain.activity.model.entity.UserRaffleOrderEntity;
import cn.hush.domain.activity.service.IRaffleActivityPartakeService;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author Hush
 * @description
 * @create 2024-11-27 上午2:09
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleActivityPartakeServiceTest {

    @Resource
    private IRaffleActivityPartakeService raffleActivityPartakeService;

    @Test
    public void text_creatOrder() {
        //請求參數
        PartakeRaffleActivityEntity partakeRaffleActivityEntity = new PartakeRaffleActivityEntity();
        partakeRaffleActivityEntity.setUserId("xiaofuge");
        partakeRaffleActivityEntity.setActivityId(100301L);
        //調用接口
        UserRaffleOrderEntity userRaffleOrder = raffleActivityPartakeService.createOrder(partakeRaffleActivityEntity);
        log.info("请求参数：{}", JSON.toJSONString(partakeRaffleActivityEntity));
        log.info("调用接口：{}", JSON.toJSONString(userRaffleOrder));
    }


}
