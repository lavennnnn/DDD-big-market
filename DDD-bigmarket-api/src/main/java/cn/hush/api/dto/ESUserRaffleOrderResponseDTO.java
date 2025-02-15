package cn.hush.api.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author Hush
 * @description 用户抽奖订单DTO数据
 * @create 2025-01-06 上午8:42
 */
@Data
public class ESUserRaffleOrderResponseDTO {

    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;
    /** 活动名称 */
    private String activityName;
    /** 抽奖策略ID */
    private Long strategyId;
    /** 订单ID */
    private String orderId;
    /** 下单时间 */
    private Date orderTime;
    /** 订单状态；create-创建、used-已使用、cancel-已作废 */
    private String orderState;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

}
