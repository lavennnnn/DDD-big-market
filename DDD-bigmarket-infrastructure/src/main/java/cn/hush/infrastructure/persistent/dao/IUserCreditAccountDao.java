package cn.hush.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.hush.infrastructure.persistent.po.UserCreditAccountPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Hush
 * @description 用户积分账户
 * @create 2024-12-12 上午11:24
 */
@Mapper
public interface IUserCreditAccountDao {

    int updateAmount(UserCreditAccountPO userCreditAccountReq);

    void insert(UserCreditAccountPO userCreditAccountReq);

    UserCreditAccountPO queryUserCreditAccount(UserCreditAccountPO userCreditAccountReq);


}
