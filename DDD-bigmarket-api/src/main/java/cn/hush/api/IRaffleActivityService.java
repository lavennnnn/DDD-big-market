package cn.hush.api;

import cn.hush.api.dto.ActivityDrawRequestDTO;
import cn.hush.api.dto.ActivityDrawResponseDTO;
import cn.hush.api.dto.UserActivityAccountRequestDTO;
import cn.hush.api.dto.UserActivityAccountResponseDTO;
import cn.hush.types.model.Response;

/**
 * @author Hush
 * @description 抽奖活动服务
 * @create 2024-11-27 下午6:17
 */
public interface IRaffleActivityService {

    /**
     * 活动装配，数据预热缓存
     * @param activityId 活动ID
     * @return 装配结果
     */
    Response<Boolean> armory(Long activityId);

    /**
     * 活动抽奖接口
     * @param request 请求对象
     * @return 返回结果
     */
    Response<ActivityDrawResponseDTO> draw(ActivityDrawRequestDTO request);


    /**
     * 日历签到返利接口
     * @param userId 用户id
     * @return 签到获得
     */
    Response<Boolean> calenderSignInRebate(String userId);

    /**
     * 判断是否完成日历签到返利
     * @param userId 用户id
     * @return 签到获得
     */
    Response<Boolean> isCalenderSignInRebateDone(String userId);

    /**
     * 查詢用户活动额度
     * @param request 查詢用户活动额度 请求对象
     * @return 额度结果
     */
    Response<UserActivityAccountResponseDTO> queryUserActivityAccount(UserActivityAccountRequestDTO request);
}
