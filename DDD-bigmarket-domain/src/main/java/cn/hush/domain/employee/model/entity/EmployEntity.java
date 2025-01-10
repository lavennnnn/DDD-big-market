package cn.hush.domain.employee.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Hush
 * @description 员工实体对象
 * @create 2025-01-07 上午2:51
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployEntity {

    private Long id;

    // 用户名 - 用于登录
    private String username;

    // 姓名
    private String name;

    private String password;

    private Integer status;

    //员工类型 admin，guest
    private String type;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


}
