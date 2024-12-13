package cn.hush.domain.credit.service;

import cn.hush.domain.credit.model.entity.TradeEntity;

/**
 * @author Hush
 * @description 积分调额接口【正逆向，增减积分】
 * @create 2024-12-13 下午6:51
 */
public interface ICreditAdjustService {

    /**
     * 创建增加积分额度订单
     * @param tradeEntity 交易实体对象
     * @return 单号
     */
    String createOrder(TradeEntity tradeEntity);

}
