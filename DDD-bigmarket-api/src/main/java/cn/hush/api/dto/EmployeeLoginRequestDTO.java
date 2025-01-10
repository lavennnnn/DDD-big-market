package cn.hush.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Hush
 * @description 员工登录请求对象
 * @create 2025-01-07 上午3:50
 */
@Data
@ApiModel(description = "员工登录时传递的数据模型")
public class EmployeeLoginRequestDTO {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

}
