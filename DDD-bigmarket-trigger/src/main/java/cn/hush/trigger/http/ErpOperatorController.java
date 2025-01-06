package cn.hush.trigger.http;

import cn.hush.adapter.repository.IESUserRaffleOrderRepository;
import cn.hush.api.IErpOperateService;
import cn.hush.api.dto.ESUserRaffleOrderResponseDTO;
import cn.hush.api.response.Response;
import cn.hush.model.valobj.ESUserRaffleOrderVO;
import cn.hush.types.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
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

}
