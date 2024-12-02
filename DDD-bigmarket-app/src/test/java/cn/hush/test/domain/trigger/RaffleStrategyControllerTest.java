package cn.hush.test.domain.trigger;

import cn.hush.api.IRaffleStrategyService;
import cn.hush.api.dto.RaffleAwardListRequestDTO;
import cn.hush.api.dto.RaffleAwardListResponseDTO;
import cn.hush.types.model.Response;
import com.alibaba.fastjson2.JSON;
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
 * @create 2024-12-01 下午12:22
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleStrategyControllerTest {

    @Resource
    private IRaffleStrategyService raffleStrategyService;

    @Test
    public void test_queryRaffleAwardList() {
        RaffleAwardListRequestDTO request = new RaffleAwardListRequestDTO();
        request.setUserId("xiaofuge");
        request.setActivityId(100301L);
        Response<List<RaffleAwardListResponseDTO>> response = raffleStrategyService.queryRaffleAwardList(request);

        log.info("请求参数 :{}", JSON.toJSONString(request));
        log.info("测试结果:{}", JSON.toJSONString(response));
    }


}
