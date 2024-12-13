package cn.hush.domain.credit.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Hush
 * @description 交易名称枚举值
 * @create 2024-12-13 下午6:46
 */
@Getter
@AllArgsConstructor
public enum TradeNameVO {

    REBATE("行为返利"),
    CONVERT_SKU("兑换抽奖"),

    ;

    private final String name;

}
