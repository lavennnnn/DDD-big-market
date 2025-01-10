package cn.hush.domain.award.service.distribute.impl;

import cn.hush.domain.award.model.aggregate.GiveOutPrizesAggregate;
import cn.hush.domain.award.model.entity.DistributeAwardEntity;
import cn.hush.domain.award.model.entity.UserAwardRecordEntity;
import cn.hush.domain.award.model.entity.UserCreditAwardEntity;
import cn.hush.domain.award.model.vo.AwardStateVO;
import cn.hush.domain.award.adapter.repository.IAwardRepository;
import cn.hush.domain.award.service.distribute.IDistributeAward;
import cn.hush.types.common.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author Hush
 * @description 用户积分奖品，支持 award_config 透传，满足黑名单积分奖励。
 * @create 2024-12-12 上午11:28
 */
@Component("user_credit_random")
public class UserCreditRandomAward implements IDistributeAward {

    @Resource
    private IAwardRepository awardRepository;

    @Override
    public void giveOutPrizes(DistributeAwardEntity distributeAwardEntity) {
        Integer awardId = distributeAwardEntity.getAwardId();
        String awardConfig = distributeAwardEntity.getAwardConfig();

        if (StringUtils.isBlank(awardConfig)) {
            // 1 - 10 积分范围 （用于随机生成范围内的一个）
            awardConfig = awardRepository.queryAwardConfig(awardId);
        }

        String[] creditRange = awardConfig.split(Constants.SPLIT);
        if (creditRange.length != 2) {
            throw new RuntimeException("award_config [" + awardConfig + "] 不是范围值");
        }

        //生成随机积分值
        BigDecimal creditAmount = generateRandomNumber(new BigDecimal(creditRange[0]), new BigDecimal(creditRange[1]));

        //构建聚合对象
        UserAwardRecordEntity userAwardRecordEntity = GiveOutPrizesAggregate.buildDistributeUserAwardRecordEntity(
                distributeAwardEntity.getUserId(),
                distributeAwardEntity.getOrderId(),
                distributeAwardEntity.getAwardId(),
                AwardStateVO.complete
        );

        UserCreditAwardEntity userCreditAwardEntity = GiveOutPrizesAggregate.buildUserCreditAwardEntity(
                distributeAwardEntity.getUserId(),
                creditAmount
        );

        GiveOutPrizesAggregate giveOutPrizesAggregate = new GiveOutPrizesAggregate();
        giveOutPrizesAggregate.setUserId(distributeAwardEntity.getUserId());
        giveOutPrizesAggregate.setUserCreditAwardEntity(userCreditAwardEntity);
        giveOutPrizesAggregate.setUserAwardRecordEntity(userAwardRecordEntity);

        //存储发奖对象
        awardRepository.saveGiveOutPrizesAggregate(giveOutPrizesAggregate);

    }

    private BigDecimal generateRandomNumber(BigDecimal min, BigDecimal max) {
        if (min.equals(max)) return min;
        BigDecimal randomBigDecimal = min.add(BigDecimal.valueOf(Math.random()).multiply(max.subtract(min)));
        return randomBigDecimal.round(new MathContext(3));
    }
}
