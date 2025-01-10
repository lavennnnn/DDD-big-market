package cn.hush.api;

import cn.hush.api.dto.ESUserRaffleOrderResponseDTO;
import cn.hush.api.dto.EmployeeLoginRequestDTO;
import cn.hush.api.dto.EmployeeLoginResponseDTO;
import cn.hush.api.response.Response;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Hush
 * @description ERP 运营接口
 * @create 2025-01-06 上午8:39
 */
@Service
public interface IErpOperateService {

     Response<List<ESUserRaffleOrderResponseDTO>> queryUserRaffleOrder();

     Response<EmployeeLoginResponseDTO> employeeLogin ( EmployeeLoginRequestDTO employeeLoginRequestDTO);

}
