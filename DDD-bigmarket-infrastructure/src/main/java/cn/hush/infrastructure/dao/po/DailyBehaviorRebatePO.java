package cn.hush.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Hush
 * @description 日常行为返利活动配置 持久化对象
 * @create 2024-12-03 下午7:10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyBehaviorRebatePO {

    /** 自增ID */
    private Long id;
    /** 行为类型（sign_in 签到、openai_pay 支付） */
    private String behaviorType;
    /** 返利描述 */
    private String rebateDesc;
    /** 返利类型（sku 活动库存充值商品、integral 用户活动积分） */
    private String rebateType;
    /** 返利配置 */
    private String rebateConfig;
    /** 状态（open 开启、close 关闭） */
    private String state;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

}
