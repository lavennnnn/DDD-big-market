package cn.hush.domain.award.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author Hush
 * @description
 * @create 2024-11-27 下午11:01
 */
@Getter
@AllArgsConstructor
public enum TaskStateVO {

    create("create", "创建"),
    complete("complete", "发送完成"),
    fail("fail", "发送失败"),
    ;
    private final String code;
    private final String desc;

}
