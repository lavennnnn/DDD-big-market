package cn.hush.domain.activity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Hush
 * @description 用户抽奖订单状态枚举
 * @create 2024-11-26 下午6:50
 */
@Getter
@AllArgsConstructor
public enum UserRaffleOrderStateVO {

    create("create", "创建"),
    used("used", "已使用"),
    cancel("cancel", "已作废"),
    ;

    private final String code;
    private final String desc;

}
