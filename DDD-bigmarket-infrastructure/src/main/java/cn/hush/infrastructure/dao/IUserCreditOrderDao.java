package cn.hush.infrastructure.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import cn.hush.infrastructure.dao.po.UserCreditOrderPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Hush
 * @description 用户积分流水单 DAO
 * @create 2024-12-13 下午6:32
 */
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserCreditOrderDao {

    void insert (UserCreditOrderPO userCreditOrderPO);

}
