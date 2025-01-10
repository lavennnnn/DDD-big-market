package cn.hush.trigger.http;


import cn.hush.api.IErpOperateService;
import cn.hush.api.dto.ESUserRaffleOrderResponseDTO;
import cn.hush.api.dto.EmployeeLoginRequestDTO;
import cn.hush.api.dto.EmployeeLoginResponseDTO;
import cn.hush.api.response.Response;
import cn.hush.domain.employee.model.entity.EmployEntity;
import cn.hush.domain.employee.repository.IEmployeeRepository;
import cn.hush.domain.employee.service.IEmployeeService;
import cn.hush.infrastructure.jwt.IJwtService;
import cn.hush.infrastructure.jwt.JwtService;
import cn.hush.querys.adapter.repository.IESUserRaffleOrderRepository;
import cn.hush.querys.model.valobj.ESUserRaffleOrderVO;
import cn.hush.types.common.Constants;
import cn.hush.types.enums.ResponseCode;
import cn.hush.types.exception.AppException;
import cn.hush.types.utils.BaseContext;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.JsonUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Hush
 * @description
 * @create 2025-01-06 上午8:32
 */
@RestController()
@Slf4j
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/raffle/erp/")
@DubboService(version = "1.0")
public class ErpOperatorController implements IErpOperateService {

    @Resource
    private IESUserRaffleOrderRepository userRaffleOrderRepository;
    @Resource
    private IEmployeeService employeeService;
    @Resource
    private IJwtService jwtService;



    /**
     * 查询运营数据，用户抽奖单列表
     * curl --request GET --url 'http://localhost:8098/api/v1/raffle/erp/query_user_raffle_order'
     */
    @Override
    @RequestMapping(value = "query_user_raffle_order", method = RequestMethod.GET)
    public Response<List<ESUserRaffleOrderResponseDTO>> queryUserRaffleOrder() {
        try {
            log.info("查询运营数据，用户抽奖单列表");
            List<ESUserRaffleOrderVO> esUserRaffleOrderVOS = userRaffleOrderRepository.queryESUserRaffleOrderVOList();
            List<ESUserRaffleOrderResponseDTO> esUserRaffleOrderResponseDTOS = new ArrayList<ESUserRaffleOrderResponseDTO>();
            for (ESUserRaffleOrderVO vo : esUserRaffleOrderVOS) {
                ESUserRaffleOrderResponseDTO esUserRaffleOrderResponseDTO = new ESUserRaffleOrderResponseDTO();
                esUserRaffleOrderResponseDTO.setUserId(vo.getUserId());
                esUserRaffleOrderResponseDTO.setActivityId(vo.getActivityId());
                esUserRaffleOrderResponseDTO.setActivityName(vo.getActivityName());
                esUserRaffleOrderResponseDTO.setStrategyId(vo.getStrategyId());
                esUserRaffleOrderResponseDTO.setOrderTime(vo.getOrderTime());
                esUserRaffleOrderResponseDTO.setOrderId(vo.getOrderId());
                esUserRaffleOrderResponseDTO.setOrderState(vo.getOrderState());
                esUserRaffleOrderResponseDTO.setCreateTime(vo.getCreateTime());
                esUserRaffleOrderResponseDTO.setUpdateTime(vo.getUpdateTime());
                esUserRaffleOrderResponseDTOS.add(esUserRaffleOrderResponseDTO);
            }

            return Response.<List<ESUserRaffleOrderResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(esUserRaffleOrderResponseDTOS)
                    .build();
        }catch (Exception e){
            log.error("查询运营数据，用户抽奖单列表，出现异常：", e);
            return Response.<List<ESUserRaffleOrderResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();

        }

    }

    @Override
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Response<EmployeeLoginResponseDTO> employeeLogin (@RequestBody EmployeeLoginRequestDTO employeeLoginRequestDTO) {
        try {
            log.info("员工登录：{}", employeeLoginRequestDTO);
            String username = employeeLoginRequestDTO.getUsername();
            String password = employeeLoginRequestDTO.getPassword();

            EmployEntity employEntity = EmployEntity.builder()
                    .username(username)
                    .password(password)
                    .build();

            EmployEntity employ = employeeService.login(employEntity);

            //登录成功后，生成jwt令牌
            String token = jwtService.buildEmpToken(employ.getId());
            EmployeeLoginResponseDTO result = EmployeeLoginResponseDTO.builder()
                    .id(employ.getId())
                    .userName(employ.getUsername())
                    .name(employ.getName())
                    .token(token)
                    .type(employ.getType())
                    .loginStatus(1)
                    .build();

            log.info("登录响应信息为:{}", JSON.toJSONString(result) );

            return Response.<EmployeeLoginResponseDTO>builder()
                    .info(ResponseCode.SUCCESS.getInfo())
                    .code(ResponseCode.SUCCESS.getCode())
                    .data(result)
                    .build();

        }catch (Exception e) {
            log.error("登录失败，出现异常：" ,e );
            return Response.<EmployeeLoginResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(EmployeeLoginResponseDTO.builder()
                            .id(null)
                            .userName(null)
                            .name(null)
                            .token(null)
                            .type(null)
                            .loginStatus(0)
                            .build())
                    .build();
        }

    }




}
