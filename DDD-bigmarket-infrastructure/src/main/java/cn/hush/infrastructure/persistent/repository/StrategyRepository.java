package cn.hush.infrastructure.persistent.repository;

import cn.hush.domain.strategy.model.entity.StrategyAwardEntity;
import cn.hush.domain.strategy.repository.IStrategyRepository;
import cn.hush.infrastructure.persistent.dao.IStrategyAwardDao;
import cn.hush.infrastructure.persistent.po.StrategyAwardPO;
import cn.hush.infrastructure.redis.IRedisService;
import cn.hush.types.common.Constants;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Hush
 * @description 策略仓储实现
 * @create 2024-08-28 下午8:55
 */
@Repository
public class StrategyRepository implements IStrategyRepository {


    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Resource
    private IRedisService redisService;

    // 查询策略奖品列表
    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        //优先从缓存获取
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId;

        List<StrategyAwardEntity> strategyAwardEntities = redisService.getValue(cacheKey);

        if (null != strategyAwardEntities && !strategyAwardEntities.isEmpty()) return strategyAwardEntities;
        else {
            //从库中获取数据
            List<StrategyAwardPO> strategyAwards = strategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);
            strategyAwardEntities = new ArrayList<>(strategyAwards.size());
            for (StrategyAwardPO strategyAward : strategyAwards) {
                StrategyAwardEntity strategyAwardEntity = StrategyAwardEntity.builder()
                        .strategyId(strategyAward.getStrategyId())
                        .awardId(strategyAward.getAwardId())
                        .awardCount(strategyAward.getAwardCount())
                        .awardCountSurplus(strategyAward.getAwardCountSurplus())
                        .awardRate(strategyAward.getAwardRate())
                        .build();
                strategyAwardEntities.add(strategyAwardEntity);
            }
            redisService.setValue(cacheKey, strategyAwardEntities);
            return strategyAwardEntities;
        }

    }

    //存到redis中
    @Override
    public void storeStrategyAwardSearchRateTables(Long strategyId, BigDecimal rateRange, HashMap<Integer, Integer> strategyAwardSearchRateTables) {
        // 1. 存储抽奖策略范围值，如10000，用于生成1000以内的随机数
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId, rateRange);
        // 2. 存储概率查找表
        Map<Integer, Integer> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId);
        cacheRateTable.putAll(strategyAwardSearchRateTables);
    }

    /**
     * 获取概率范围
     */
    @Override
    public BigDecimal getRateRange(Long strategyId) {
        //todo [ null pointer exception ]
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId);
    }

    @Override
    public Integer getStrategyAwardAssemble(Long strategyId, int rateKey) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId, rateKey);
    }
}
