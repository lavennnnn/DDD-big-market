package cn.hush.infrastructure.dao;

import cn.hush.infrastructure.dao.po.UserCreditAccountPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Hush
 * @description 用户积分账户
 * @create 2024-12-12 上午11:24
 */
@Mapper
public interface IUserCreditAccountDao {

    int updateAmountAdd(UserCreditAccountPO userCreditAccountReq);

    void insert(UserCreditAccountPO userCreditAccountReq);

    UserCreditAccountPO queryUserCreditAccount(UserCreditAccountPO userCreditAccountReq);

    int updateAmountSubtraction(UserCreditAccountPO userCreditAccountReq);

}
