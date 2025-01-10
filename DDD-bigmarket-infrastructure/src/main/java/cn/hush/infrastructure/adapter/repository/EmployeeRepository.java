package cn.hush.infrastructure.adapter.repository;

import cn.hush.domain.employee.model.entity.EmployEntity;
import cn.hush.domain.employee.repository.IEmployeeRepository;
import cn.hush.infrastructure.dao.IEmployeeDao;
import cn.hush.infrastructure.dao.po.EmployeePO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author Hush
 * @description
 * @create 2025-01-07 上午3:15
 */
@Slf4j
@Repository
public class EmployeeRepository implements IEmployeeRepository {

    @Resource
    private IEmployeeDao employeeDao;

    //通过用户名查询员工信息
    @Override
    public EmployEntity queryEmployeeByUsername(EmployEntity employee) {

        String username = employee.getUsername();

        EmployeePO emp = employeeDao.getEmpByUsername(username);

        return EmployEntity.builder()
                .username(emp.getUsername())
                .password(emp.getPassword())
                .id(emp.getId())
                .type(emp.getType())
                .name(emp.getName())
                .status(emp.getStatus())
                .createTime(emp.getCreateTime())
                .updateTime(emp.getUpdateTime())
                .build();
    }

}
