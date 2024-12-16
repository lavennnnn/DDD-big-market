package cn.hush.domain.activity.model.entity;

import cn.hush.domain.activity.model.vo.OrderTradeTypeVO;
import lombok.Data;

/**
 * @author Hush
 * @description 活动商品充值实体对象
 * @create 2024-11-20 上午12:42
 */
@Data
public class SkuRechargeEntity {

    /** 用户ID */
    private String userId;
    /** 商品SKU - activity + activity count */
    private Long sku;
    /** 幂等业务单号，外部谁充值谁透传，这样来保证幂等（多次调用也能确保结果唯一，不会多次充值）。 */
    private String outBusinessNo;
    // 交易类型
    private OrderTradeTypeVO orderTradeType = OrderTradeTypeVO.rebate_no_pay_trade;

}
