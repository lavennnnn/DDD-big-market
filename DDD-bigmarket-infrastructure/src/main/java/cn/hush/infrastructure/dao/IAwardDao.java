package cn.hush.infrastructure.dao;


import cn.hush.infrastructure.dao.po.AwardPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 奖品表
 */

@Mapper
public interface IAwardDao {

    List<AwardPO> queryAwardList();

    String queryAwardConfigByAwardId(Integer awardId);

    String queryAwardKeyByAwardId(Integer awardId);
}
