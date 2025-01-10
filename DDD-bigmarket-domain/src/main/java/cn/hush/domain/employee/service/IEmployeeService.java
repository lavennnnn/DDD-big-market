package cn.hush.domain.employee.service;

import cn.hush.domain.employee.model.entity.EmployEntity;

/**
 * @author Hush
 * @description 员工操作服务
 * @create 2025-01-07 上午2:49
 */
public interface IEmployeeService {

    // 员工登录
    EmployEntity login(EmployEntity employEntity);


}
