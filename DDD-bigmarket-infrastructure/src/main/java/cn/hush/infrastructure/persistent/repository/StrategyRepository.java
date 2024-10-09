package cn.hush.infrastructure.persistent.repository;

import cn.hush.domain.strategy.model.entity.StrategyAwardEntity;
import cn.hush.domain.strategy.model.entity.StrategyEntity;
import cn.hush.domain.strategy.model.entity.StrategyRuleEntity;
import cn.hush.domain.strategy.repository.IStrategyRepository;
import cn.hush.infrastructure.persistent.dao.IStrategyAwardDao;
import cn.hush.infrastructure.persistent.dao.IStrategyDao;
import cn.hush.infrastructure.persistent.dao.IStrategyRuleDao;
import cn.hush.infrastructure.persistent.po.StrategyAwardPO;
import cn.hush.infrastructure.persistent.po.StrategyPO;
import cn.hush.infrastructure.persistent.po.StrategyRulePO;
import cn.hush.infrastructure.redis.IRedisService;
import cn.hush.types.common.Constants;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Hush
 * @description 策略仓储实现
 * @create 2024-08-28 下午8:55
 */
@Repository
public class StrategyRepository implements IStrategyRepository {

    @Resource
    private IStrategyDao strategyDao;

    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Resource
    private IRedisService redisService;

    @Resource
    private IStrategyRuleDao strategyRuleDao;

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
    public void storeStrategyAwardSearchRateTables(String key, Integer rateRange, Map<Integer, Integer> strategyAwardSearchRateTables) {
        // 1. 存储抽奖策略范围值，如10000，用于生成1000以内的随机数
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key, rateRange);
        // 2. 存储概率查找表
        Map<Integer, Integer> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key);
        cacheRateTable.putAll(strategyAwardSearchRateTables);
    }

    /**
     * 获取概率范围
     */
    @Override
    public int getRateRange(Long strategyId) {
        return getRateRange(String.valueOf(strategyId));
    }

    @Override
    public int getRateRange(String key) {
         return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key);
    }

    @Override
    public Integer getStrategyAwardAssemble(String key, Integer rateKey) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key, rateKey);
    }

    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Long strategyId) {
        //优先从缓存获取
        String cacheKey = Constants.RedisKey.STRATEGY_KEY + strategyId;
        StrategyEntity strategyEntity = redisService.getValue(cacheKey);
        if (null != strategyEntity) return strategyEntity;

        //缓存中没有，从数据库中获取
        StrategyPO strategyPO = strategyDao.queryStrategyByStrategyId(strategyId);

        strategyEntity = StrategyEntity.builder()
                .strategyId(strategyPO.getStrategyId())
                .strategyDesc(strategyPO.getStrategyDesc())
                .ruleModels(strategyPO.getRuleModels())
                .build();

        // 存入缓存
        redisService.setValue(cacheKey, strategyEntity);
        return strategyEntity;
    }

    @Override
    public StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel) {
        // 程序简单，直接查库
        StrategyRulePO strategyRuleReq = new StrategyRulePO();
        strategyRuleReq.setStrategyId(strategyId);
        strategyRuleReq.setRuleModel(ruleModel);
        StrategyRulePO strategyRuleRes = strategyRuleDao.queryStrategyRule(strategyRuleReq);
        return StrategyRuleEntity.builder()
                .strategyId(strategyRuleRes.getStrategyId())
                .awardId(strategyRuleRes.getAwardId())
                .ruleType(strategyRuleRes.getRuleType())
                .ruleModel(strategyRuleRes.getRuleModel())
                .ruleValue(strategyRuleRes.getRuleValue())
                .ruleDesc(strategyRuleRes.getRuleDesc())
                .build();

    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel) {
        StrategyRulePO strategyRule = new StrategyRulePO();
        strategyRule.setStrategyId(strategyId);
        strategyRule.setAwardId(awardId);
        strategyRule.setRuleModel(ruleModel);
        return strategyRuleDao.queryStrategyRuleValue(strategyRule);

    }
}










