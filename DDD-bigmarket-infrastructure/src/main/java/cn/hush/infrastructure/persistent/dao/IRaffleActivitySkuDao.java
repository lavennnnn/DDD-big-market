package cn.hush.infrastructure.persistent.dao;

import cn.hush.infrastructure.persistent.po.RaffleActivitySkuPO;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.ManagedBean;

/**
 * @author Hush
 * @description
 * @create 2024-11-16 上午4:50
 */
@Mapper
public interface IRaffleActivitySkuDao {

    RaffleActivitySkuPO queryActivitySku(Long sku);

}
