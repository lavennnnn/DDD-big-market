package cn.hush.infrastructure.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Hush
 * @description 调额响应对象
 * @create 2025-01-10 下午2:57
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdjustQuotaResponseDTO {

    /**
     * 总量额度
     */
    private Integer totalQuota;
    /**
     * 剩余额度
     */
    private Integer surplusQuota;

}
