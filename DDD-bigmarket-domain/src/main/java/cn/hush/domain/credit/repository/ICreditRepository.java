package cn.hush.domain.credit.repository;

import cn.hush.domain.credit.model.aggregate.TradeAggregate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author Hush
 * @description 用户积分仓储接口
 * @create 2024-12-13 下午6:41
 */

public interface ICreditRepository {

    void saveUserCreditTradeOrder(TradeAggregate tradeAggregate);

}
