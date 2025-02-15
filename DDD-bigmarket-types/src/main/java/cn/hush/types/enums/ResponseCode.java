package cn.hush.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Hush
 * @description
 * @create 2024-09-02 下午11:15
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("0000", "成功"),
    UN_ERROR("0001", "未知失败"),
    ILLEGAL_PARAMETER("0002", "非法参数"),
    INDEX_DUP("0003","唯一索引冲突"),
    DEGRADE_SWITCH("0004", "活动已降级"),
    RATE_LIMITER("0005", "限流-访问拦截"),
    HYSTRIX("0006", "访问超时熔断"),
    GATEWAY_ERROR("0007", "网关接口调用失败"),
    STRATEGY_RULE_WEIGHT_IS_NULL("ERR_BIZ_001", "业务异常，策略规则中 rule_weight 权重规则已适用但未配置"),
    UN_ASSEMBLED_STRATEGY_ARMORY("ERR_BIZ_002", "抽奖策略配置未装配。请通过IStrategyArmory完成装配"),
    ACTIVITY_STATE_ERROR("ERR_BIZ_003", "活动未开启（非open状态）"),
    ACTIVITY_DATE_ERROR("ERR_BIZ_004", "非活动日期范围"),
    ACTIVITY_SKU_STOCK_ERROR("ERR_BIZ_005", "活动库存不足"),
    ACCOUNT_QUOTA_ERROR("ERR_BIZ_006","账户总额度不足"),
    ACCOUNT_MONTH_QUOTA_ERROR("ERR_BIZ_007","账户月额度不足"),
    ACCOUNT_DAY_QUOTA_ERROR("ERR_BIZ_008","账户日额度不足"),
    ACCOUNT_ORDER_ERROR("ERR_BIZ_009","用户抽奖单已经使用过，不可重复抽奖"),
    USER_CREDIT_ACCOUNT_NO_AVAILABLE_AMOUNT("ERR_CREDIT_001", "用户积分账户额度不足"),
    EMPLOYEE_ACCOUNT_NOT_FOUND("ERR_BIZ_010","该员工不存在"),
    PASSWORD_ERROR("ERR_BIZ_011","密码错误"),
    ACCOUNT_LOCKED("ERR_BIZ_012","该账号已被冻结，无法登录"),

    ;

    private String code;
    private String info;


}
