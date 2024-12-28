package cn.hush.infrastructure.elasticsearch;

import cn.hush.infrastructure.elasticsearch.po.UserRaffleOrderPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Hush
 * @description
 * @create 2024-12-28 下午3:54
 */
@Mapper
public interface IElasticSearchUserRaffleOrderDao {

    List<UserRaffleOrderPO> queryUserRaffleOrderList();

}
