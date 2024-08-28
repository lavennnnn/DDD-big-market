package cn.hush.infrastructure.persistent.dao;


import cn.hush.infrastructure.persistent.po.AwardPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 奖品表
 */

@Mapper
public interface IAwardDao {

    List<AwardPO> queryAwardList();

}
