package cn.hush.domain.activity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Hush
 * @description 活动sku库存 key 值对象
 * @create 2024-11-21 上午4:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivitySkuStockKeyVO {

    /**
     * 商品sku
     */
    private Long sku;

    /**
     * 活动id
     */
    private Long activityId;

}
