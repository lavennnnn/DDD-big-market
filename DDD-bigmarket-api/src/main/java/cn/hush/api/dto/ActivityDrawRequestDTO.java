package cn.hush.api.dto;

import lombok.Data;

/**
 * @author Hush
 * @description 活动抽奖请求对象
 * @create 2024-11-27 下午6:20
 */
@Data
public class ActivityDrawRequestDTO {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动ID
     */
    private Long activityId;


}
