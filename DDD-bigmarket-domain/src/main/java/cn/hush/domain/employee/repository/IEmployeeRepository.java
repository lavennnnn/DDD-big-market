package cn.hush.domain.employee.repository;

import cn.hush.domain.employee.model.entity.EmployEntity;

/**
 * @author Hush
 * @description
 * @create 2025-01-07 上午3:14
 */
public interface IEmployeeRepository {

    EmployEntity queryEmployeeByUsername(EmployEntity employee);

}
