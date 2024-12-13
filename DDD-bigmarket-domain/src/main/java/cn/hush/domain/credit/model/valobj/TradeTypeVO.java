package cn.hush.domain.credit.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Hush
 * @description 交易类型枚举值
 * @create 2024-12-13 下午6:46
 */
@Getter
@AllArgsConstructor
public enum TradeTypeVO {

    FORWARD("forward", "正向交易，+ 积分"),
    REVERSE("reverse", "逆向交易，- 积分"),

    ;

    private final String code;
    private final String info;

}
