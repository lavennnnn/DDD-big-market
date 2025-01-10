package cn.hush.querys.adapter.repository;

import cn.hush.querys.model.valobj.ESUserRaffleOrderVO;

import java.util.List;

/**
 * @author Hush
 * @description 用户抽奖订单表查询
 * @create 2025-01-07 上午1:42
 */
public interface IESUserRaffleOrderRepository {

    List<ESUserRaffleOrderVO> queryESUserRaffleOrderVOList();


}
