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
    STRATEGY_RULE_WEIGHT_IS_NULL("ERR_BIZ_001", "业务异常，策略规则中 rule_weight 权重规则已适用但未配置"),
    ;

    private String code;
    private String info;


}
