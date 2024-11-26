package cn.hush.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Hush
 * @description 参与抽奖活动实体对象
 * @create 2024-11-26 下午6:46
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartakeRaffleActivityEntity {

    /** 用户ID */
    private String userId;

    /** 活动ID */
    private Long activityId;

}
