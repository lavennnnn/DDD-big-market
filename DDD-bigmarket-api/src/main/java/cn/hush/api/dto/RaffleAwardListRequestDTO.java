package cn.hush.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Hush
 * @description 抽奖奖品列表，请求对象
 * @create 2024-11-06 下午5:12
 */
@Data
public class RaffleAwardListRequestDTO {

    // 用户ID
    private String userId;

    // 活动ID
    private Long activityId;


}
