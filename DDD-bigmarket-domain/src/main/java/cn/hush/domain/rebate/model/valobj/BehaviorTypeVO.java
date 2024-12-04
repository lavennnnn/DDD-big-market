package cn.hush.domain.rebate.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Hush
 * @description 行为类型枚举值对象
 * @create 2024-12-04 下午8:49
 */
@Getter
@AllArgsConstructor
public enum BehaviorTypeVO {

    SIGN_IN("sign_in", "签到（日历）"),
    OPENAI_PAI("openai_pay", "openai 外部支付完成"),
    ;


    private final String code;
    private final String info;

}
