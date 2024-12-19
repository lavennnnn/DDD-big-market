package cn.hush.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Hush
 * @description
 * @create 2024-12-17 下午5:25
 */
@Data
public class SkuProductShopCartRequestDTO implements Serializable {

    private String userId;

    private Long sku;

}
