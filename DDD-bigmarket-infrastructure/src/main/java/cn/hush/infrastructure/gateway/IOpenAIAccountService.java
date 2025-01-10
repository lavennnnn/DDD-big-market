package cn.hush.infrastructure.gateway;

import cn.hush.infrastructure.gateway.Response.Response;
import cn.hush.infrastructure.gateway.dto.AdjustQuotaRequestDTO;
import cn.hush.infrastructure.gateway.dto.AdjustQuotaResponseDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author Hush
 * @description OpenAI应用项目账户服务接口
 * @create 2025-01-10 下午2:55
 */
public interface IOpenAIAccountService {

    @POST("/api/v1/account/adjust_quota")
    Call<Response<AdjustQuotaResponseDTO>> adjustQuota(@Body AdjustQuotaRequestDTO requestDTO);

}
