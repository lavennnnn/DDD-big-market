package cn.hush.infrastructure.adapter.port;

import cn.hush.domain.award.adapter.port.IAwardPort;
import cn.hush.infrastructure.gateway.IOpenAIAccountService;
import cn.hush.infrastructure.gateway.Response.Response;
import cn.hush.infrastructure.gateway.dto.AdjustQuotaRequestDTO;
import cn.hush.infrastructure.gateway.dto.AdjustQuotaResponseDTO;
import cn.hush.types.enums.ResponseCode;
import cn.hush.types.exception.AppException;
import org.springframework.beans.factory.annotation.Value;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import javax.annotation.Resource;

/**
 * @author Hush
 * @description
 * @create 2025-01-10 下午2:49
 */
@Slf4j
@Service
public class AwardPort implements IAwardPort {

    @Value("${gateway.config.big-market-appId}")
    private String BIG_MARKET_APPID;
    @Value("${gateway.config.big-market-appToken}")
    private String BIG_MARKET_APPTOKEN;

    @Resource
    private IOpenAIAccountService openAIAccountService;

    @Override
    public void adjustAmount(String userId, Integer increaseQuota) throws Exception {
        try {
            AdjustQuotaRequestDTO requestDTO = AdjustQuotaRequestDTO.builder()
                    .appId(BIG_MARKET_APPID)
                    .appToken(BIG_MARKET_APPTOKEN)
                    .openid(userId)
                    .increaseQuota(increaseQuota)
                    .build();

            Call<Response<AdjustQuotaResponseDTO>> call = openAIAccountService.adjustQuota(requestDTO);
            Response<AdjustQuotaResponseDTO> response = call.execute().body();
            log.info("请求OpenAI应用账户调额接口完成 userId:{} increaseQuota:{} response:{}",
                    userId, increaseQuota, JSON.toJSONString(response));

            if (null == response || null == response.getCode() || !"0000".equals(response.getCode())) {
                throw new AppException(ResponseCode.GATEWAY_ERROR.getCode(),
                        ResponseCode.GATEWAY_ERROR.getInfo());
            }
        } catch (Exception e) {
            log.error("请求OpenAI应用账户调额接口失败 userId:{} increaseQuota:{}",
                    userId, increaseQuota, e);
            throw e;
        }
    }

}
