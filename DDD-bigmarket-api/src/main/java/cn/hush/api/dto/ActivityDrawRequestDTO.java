package cn.hush.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Hush
 * @description 活动抽奖 请求对象
 * @create 2024-11-27 下午6:20
 */
@Data
public class ActivityDrawRequestDTO implements Serializable {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动ID
     */
    private Long activityId;


}
