package cn.hush.api;

import cn.hush.api.dto.ESUserRaffleOrderResponseDTO;
import cn.hush.api.response.Response;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Hush
 * @description ERP 运营接口
 * @create 2025-01-06 上午8:39
 */
@Service
public interface IErpOperateService {

     Response<List<ESUserRaffleOrderResponseDTO>> queryUserRaffleOrder();

}
