package cn.hush.domain.employee.service;

import cn.hush.domain.employee.model.entity.EmployEntity;
import cn.hush.domain.employee.repository.IEmployeeRepository;
import cn.hush.types.common.Constants;
import cn.hush.types.enums.ResponseCode;
import cn.hush.types.exception.AppException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author Hush
 * @description 员工操作实现类
 * @create 2025-01-07 上午2:57
 */
@Service
public class EmployeeService implements IEmployeeService {

    @Resource
    private IEmployeeRepository employeeRepository;


    /**
     * 员工登录
     * @param employEntity
     * @return
     */
    @Override
    public EmployEntity login(EmployEntity employEntity) {

        String password = employEntity.getPassword();

        // 1. 根据用户名查询数据库中的数据
        EmployEntity employ = employeeRepository.queryEmployeeByUsername(employEntity);
        if (employ == null) {
            throw new AppException(ResponseCode.EMPLOYEE_ACCOUNT_NOT_FOUND.getCode());
        }

        // 2. 检验操作
        //对前端传递过来的明文密码进行md5加密，然后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employ.getPassword())) {
            throw new AppException(ResponseCode.PASSWORD_ERROR.getCode());
        }else if (Constants.StatusConstant.DISABLE.equals(employ.getStatus())) {
            throw new AppException(ResponseCode.ACCOUNT_LOCKED.getCode());
        }


        return employ;
    }

}
