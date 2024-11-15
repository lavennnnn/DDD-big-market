package cn.hush.domain.activity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @author Hush
 * @description 活动状态值对象
 * @create 2024-11-16 上午3:47
 */
@Getter
@AllArgsConstructor
public enum ActivityStateVO {

    create("create", "创建"),
    open("open", "开启"),
    close("close", "关闭"),
    ;

    private final String code;
    private final String desc;


}
