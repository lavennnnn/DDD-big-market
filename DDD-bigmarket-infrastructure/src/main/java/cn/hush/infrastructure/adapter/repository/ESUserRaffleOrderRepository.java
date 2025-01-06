package cn.hush.infrastructure.adapter.repository;

import cn.hush.adapter.repository.IESUserRaffleOrderRepository;
import cn.hush.infrastructure.elasticsearch.IElasticSearchUserRaffleOrderDao;
import cn.hush.infrastructure.elasticsearch.po.UserRaffleOrderPO;
import cn.hush.model.valobj.ESUserRaffleOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hush
 * @description
 * @create 2025-01-06 上午8:30
 */
@Slf4j
@Repository
public class ESUserRaffleOrderRepository implements IESUserRaffleOrderRepository {

    @Resource
    private IElasticSearchUserRaffleOrderDao elasticSearchUserRaffleOrderDao;

    @Override
    public List<ESUserRaffleOrderVO> queryESUserRaffleOrderVOList() {
        //查询数据
        List<UserRaffleOrderPO> userRaffleOrderPOS = elasticSearchUserRaffleOrderDao.queryUserRaffleOrderList();

        //封装数据
        ArrayList<ESUserRaffleOrderVO> esUserRaffleOrderVOs = new ArrayList<>();
        for (UserRaffleOrderPO po : userRaffleOrderPOS) {
            ESUserRaffleOrderVO esUserRaffleOrderVO = ESUserRaffleOrderVO.builder()
                    .userId(po.getUserId())
                    .activityId(po.getActivityId())
                    .activityName(po.getActivityName())
                    .orderId(po.getOrderId())
                    .orderTime(po.getOrderTime())
                    .orderState(po.getOrderState())
                    .strategyId(po.getStrategyId())
                    .createTime(po.getCreateTime())
                    .updateTime(po.getUpdateTime())
                    .build();
            esUserRaffleOrderVOs.add(esUserRaffleOrderVO);
        }
        return esUserRaffleOrderVOs;
    }

}
