package cn.hush.infrastructure.dao;

import cn.hush.infrastructure.dao.po.EmployeePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Hush
 * @description 员工表 Dao
 * @create 2025-01-07 上午2:36
 */
@Mapper
public interface IEmployeeDao {

    void alterEmpStateById(EmployeePO employeePO);

    EmployeePO getEmpByUsername(String username);

}
