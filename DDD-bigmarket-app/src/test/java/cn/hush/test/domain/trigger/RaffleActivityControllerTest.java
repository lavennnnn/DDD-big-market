package cn.hush.test.domain.trigger;

import cn.hush.api.IRaffleActivityService;
import cn.hush.api.dto.ActivityDrawRequestDTO;
import cn.hush.api.dto.ActivityDrawResponseDTO;
import cn.hush.api.dto.UserActivityAccountRequestDTO;
import cn.hush.api.dto.UserActivityAccountResponseDTO;
import cn.hush.types.model.Response;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author Hush
 * @description
 * @create 2024-12-01 下午1:20
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleActivityControllerTest {

    @Resource
    private IRaffleActivityService raffleActivityService;

    @Test
    public void test_armory() {
        Response<Boolean> response = raffleActivityService.armory(100301L);
        log.info("测试结果: {}", JSON.toJSONString(response));
    }

    @Test
    public void test_draw() {
        ActivityDrawRequestDTO request = new ActivityDrawRequestDTO();
        request.setActivityId(100301L);
        request.setUserId("xiaofuge1");
        Response<ActivityDrawResponseDTO> response = raffleActivityService.draw(request);

        log.info("请求参数: {}", JSON.toJSONString(request));
        log.info("测试结果: {}", JSON.toJSONString(response));
    }

    @Test
    public void test_signIn() {

        Response<Boolean> response = raffleActivityService.calenderSignInRebate("xiaofuge");
        log.info("测试结果:{}", JSON.toJSONString(response));


    }


    @Test
    public void test_calendarSignRebate() {
        Response<Boolean> response = raffleActivityService.calenderSignInRebate("xiaofuge");
        log.info("测试结果：{}", JSON.toJSONString(response));
    }

    @Test
    public void test_isCalendarSignRebate() {
        Response<Boolean> response = raffleActivityService.isCalenderSignInRebateDone("xiaofuge");
        log.info("测试结果：{}", JSON.toJSONString(response));
    }


    @Test
    public void test_queryUserActivityAccount() {
        UserActivityAccountRequestDTO request = new UserActivityAccountRequestDTO();
        request.setActivityId(100301L);
        request.setUserId("xiaofuge1");

        // 查询数据
        Response<UserActivityAccountResponseDTO> response = raffleActivityService.queryUserActivityAccount(request);

        log.info("请求参数：{}", JSON.toJSONString(request));
        log.info("测试结果：{}", JSON.toJSONString(response));
    }



}
