package cn.hush.domain.award.service.distribute.impl;

import cn.hush.domain.award.adapter.port.IAwardPort;
import cn.hush.domain.award.model.entity.DistributeAwardEntity;
import cn.hush.domain.award.adapter.repository.IAwardRepository;
import cn.hush.domain.award.service.distribute.IDistributeAward;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Hush
 * @description openAI账户调额
 * @create 2025-01-09 下午10:44
 */
@Component("openai_use_count")
public class OpenAlAccountAdjustQuotaAward implements IDistributeAward {

    @Resource
    private IAwardPort port;

    @Resource
    private IAwardRepository repository;

    @Override
    public void giveOutPrizes(DistributeAwardEntity distributeAwardEntity) throws Exception {
        //奖品id
        Integer awardId = distributeAwardEntity.getAwardId();

        String awardConfig = distributeAwardEntity.getAwardConfig();
        if (StringUtils.isBlank(awardConfig)) {
            awardConfig = repository.queryAwardConfig(awardId);
        }

        //发奖
        port.adjustAmount(distributeAwardEntity.getUserId(),Integer.valueOf(awardConfig) );
    }
}
