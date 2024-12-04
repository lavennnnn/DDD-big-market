package cn.hush.domain.rebate.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Hush
 * @description 任务状态值对象
 * @create 2024-12-04 下午10:16
 */
@Getter
@AllArgsConstructor
public enum TaskStateVO {

    create("create" , "创建"),
    complete("complete", "完成"),
    fail("fail", "失败"),
    ;

    private final String code;
    private final String desc;



}
