package cn.hush.domain.award.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author Hush
 * @description 账户状态枚举
 * @create 2024-12-12 下午2:44
 */
@AllArgsConstructor
@Getter
public enum AccountStatusVO {

    open("open", "开启"),
    close("close", "冻结"),
    ;

    private final String code;
    private final String desc;

}
