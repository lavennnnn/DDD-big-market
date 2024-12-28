package cn.hush.infrastructure.dao;

import cn.hush.infrastructure.dao.po.RaffleActivitySkuPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * @author Hush
 * @description
 * @create 2024-11-16 上午4:50
 */
@Mapper
public interface IRaffleActivitySkuDao {

    RaffleActivitySkuPO queryActivitySku(Long sku);

    void updateActivitySkuStock(Long sku);

    void clearActivitySkuStock(Long sku);

    List<RaffleActivitySkuPO> queryActivitySkuListByActivityId(Long activityId);
}
