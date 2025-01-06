package cn.hush.adapter.repository;

import cn.hush.model.valobj.ESUserRaffleOrderVO;

import java.util.List;

/**
 * @author Hush
 * @description
 * @create 2025-01-06 上午8:31
 */
public interface IESUserRaffleOrderRepository {

    List<ESUserRaffleOrderVO> queryESUserRaffleOrderVOList();

}
