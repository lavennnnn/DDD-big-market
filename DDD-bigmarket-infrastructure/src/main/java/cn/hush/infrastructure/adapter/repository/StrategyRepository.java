package cn.hush.infrastructure.adapter.repository;

import cn.hush.domain.strategy.model.entity.StrategyAwardEntity;
import cn.hush.domain.strategy.model.entity.StrategyEntity;
import cn.hush.domain.strategy.model.entity.StrategyRuleEntity;
import cn.hush.domain.strategy.model.vo.*;
import cn.hush.domain.strategy.repository.IStrategyRepository;
import cn.hush.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import cn.hush.infrastructure.dao.*;
import cn.hush.infrastructure.dao.po.*;
import cn.hush.infrastructure.redis.IRedisService;
import cn.hush.types.common.Constants;
import cn.hush.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static cn.hush.types.enums.ResponseCode.UN_ASSEMBLED_STRATEGY_ARMORY;

/**
 * @author Hush
 * @description 策略仓储实现
 * @create 2024-08-28 下午8:55
 */
@Slf4j
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

    @Resource
    private IRuleTreeDao ruleTreeDao;

    @Resource
    private IRuleTreeNodeDao ruleTreeNodeDao;

    @Resource
    private IRuleTreeNodeLineDao ruleTreeNodeLineDao;

    @Resource
    private IRaffleActivityDao raffleActivityDao;

    @Resource
    private IRaffleActivityAccountDao raffleActivityAccountDao;

    @Resource
    private IRaffleActivityAccountDayDao raffleActivityAccountDayDao;

    // 查询策略奖品列表
    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        //优先从缓存获取
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_LIST_KEY + strategyId;

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
                        .awardTitle(strategyAward.getAwardTitle())
                        .awardSubtitle(strategyAward.getAwardSubtitle())
                        .awardCount(strategyAward.getAwardCount())
                        .awardCountSurplus(strategyAward.getAwardCountSurplus())
                        .awardRate(strategyAward.getAwardRate())
                        .sort(strategyAward.getSort())
                        .ruleModels(strategyAward.getRuleModels())
                        .build();
                strategyAwardEntities.add(strategyAwardEntity);
            }
            redisService.setValue(cacheKey, strategyAwardEntities);
            return strategyAwardEntities;
        }

    }

    //存到redis中
    @Override
    public <K,V> void storeStrategyAwardSearchRateTables(String key, Integer rateRange, Map<K,V> strategyAwardSearchRateTables) {
        // 1. 存储抽奖策略范围值，如10000，用于生成1000以内的随机数
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key, rateRange);
        // 2. 存储概率查找表
        String tableCacheKey = Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key;
        if (redisService.isExists(tableCacheKey)) {
            redisService.remove(tableCacheKey);
        }
        Map<K,V> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key);
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
        String cacheKey = Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key;
        if (!redisService.isExists(cacheKey)){
            throw new AppException(UN_ASSEMBLED_STRATEGY_ARMORY.getCode(), cacheKey + Constants.COLON + UN_ASSEMBLED_STRATEGY_ARMORY.getInfo());
        }
        return redisService.getValue(cacheKey);
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
        if (null == strategyPO) return StrategyEntity.builder().build();
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

    @Override
    public String queryStrategyRuleValue(Long strategyId, String ruleModel) {
        return queryStrategyRuleValue(strategyId, null, ruleModel);
    }

    @Override
    public StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId) {
        StrategyAwardPO strategyAwardPO = new StrategyAwardPO();
        strategyAwardPO.setStrategyId(strategyId);
        strategyAwardPO.setAwardId(awardId);
        String ruleModel = strategyAwardDao.queryStrategyRuleModelValue(strategyAwardPO);
        return StrategyAwardRuleModelVO.builder().ruleModels(ruleModel).build();
    }

    @Override
    public RuleTreeVO queryRuleTreeVOByTreeId(String treeId) {
        //0. 优先从缓存获取
        String cacheKey = Constants.RedisKey.RULE_TREE_VO_KEY + treeId;
        RuleTreeVO ruleTreeVOCache = redisService.getValue(cacheKey);
        if (null != ruleTreeVOCache) return ruleTreeVOCache;
        //从数据库获取
        RuleTreePO ruleTreePO = ruleTreeDao.queryRuleTreePOByTreeId(treeId);
        List<RuleTreeNodePO> ruleTreeNodePOs = ruleTreeNodeDao.queryRuleTreeNodePOListByTreeId(treeId);
        List<RuleTreeNodeLinePO> ruleTreeNodeLinePOs = ruleTreeNodeLineDao.queryRuleTreeNodeLinePOListByTreeId(treeId);

        //1. tree node line 转换 Map 结构，ruleTreeNodeLineMap 的 key 是 RuleNodeFrom
        HashMap<String, List<RuleTreeNodeLineVO>> ruleTreeNodeLineMap = new HashMap<>();
        for(RuleTreeNodeLinePO ruleTreeNodeLinePO : ruleTreeNodeLinePOs ) {
            RuleTreeNodeLineVO ruleTreeNodeLineVO = RuleTreeNodeLineVO.builder()
                    .treeId(ruleTreeNodeLinePO.getTreeId())
                    .ruleNodeFrom(ruleTreeNodeLinePO.getRuleNodeFrom())
                    .ruleNodeTo(ruleTreeNodeLinePO.getRuleNodeTo())
                    .ruleLimitType(RuleLimitTypeVO.valueOf(ruleTreeNodeLinePO.getRuleLimitType()))
                    .ruleLimitValue(RuleLogicCheckTypeVO.valueOf(ruleTreeNodeLinePO.getRuleLimitValue()))
                    .build();

            List<RuleTreeNodeLineVO> ruleTreeNodeLineVOList = ruleTreeNodeLineMap.
                    computeIfAbsent(ruleTreeNodeLinePO.getRuleNodeFrom(), k -> new ArrayList<>());
            ruleTreeNodeLineVOList.add(ruleTreeNodeLineVO);
        }

        // 2. tree node 转换为Map结构
        Map<String, RuleTreeNodeVO> ruleTreeNodeVOMap = new HashMap<>();
        for (RuleTreeNodePO ruleTreeNodePO : ruleTreeNodePOs) {
            RuleTreeNodeVO ruleTreeNodeVO = RuleTreeNodeVO.builder()
                    .treeId(ruleTreeNodePO.getTreeId())
                    .ruleDesc(ruleTreeNodePO.getRuleDesc())
                    .ruleValue(ruleTreeNodePO.getRuleValue())
                    .ruleKey(ruleTreeNodePO.getRuleKey())
                    .treeNodeLineVOList(ruleTreeNodeLineMap.get(ruleTreeNodePO.getRuleKey()))
                    .build();
            ruleTreeNodeVOMap.put(ruleTreeNodePO.getRuleKey(), ruleTreeNodeVO);
        }

        // 构建 Rule Tree

        RuleTreeVO ruleTreeVO = RuleTreeVO.builder()
                .treeId(ruleTreePO.getTreeId())
                .treeDesc(ruleTreePO.getTreeDesc())
                .treeName(ruleTreePO.getTreeName())
                .treeRootRuleNode(ruleTreePO.getTreeRootRuleKey())
                .treeNodeMap(ruleTreeNodeVOMap)
                .build();

        redisService.setValue(cacheKey, ruleTreeVO);
        return ruleTreeVO;
    }

    @Override
    public void cacheStrategyAwardCount(String cacheKey, Integer awardCount) {
        if (redisService.isExists(cacheKey)) return;
        redisService.setAtomicLong(cacheKey, awardCount);
    }

    @Override
    public Boolean subtractAwardStock(String cacheKey) {
        return subtractAwardStock(cacheKey, null);
    }

    @Override
    public Boolean subtractAwardStock(String cacheKey, Date endDateTime) {
        long surplus = redisService.decr(cacheKey);
        if (surplus < 0) {
            //库存小于0，恢复为零
            redisService.setValue(cacheKey, 0);
            return false;
        }

        // 1. 按照cacheKey decr 后的值，如 99、98 和 key 组成为库存锁的 key
        // 2. 加锁为了兜底，如果后续有恢复库存，手动处理等，也不会超卖，因为所有可用库存的 key 都被加锁了
        String lockKey = cacheKey + Constants.UNDERLINE + surplus;
        Boolean lock = false;
        if (null != endDateTime) {
            long expireMillis = endDateTime.getTime() - System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
            lock = redisService.setNx(lockKey, expireMillis, TimeUnit.MILLISECONDS);
        } else {
            lock = redisService.setNx(lockKey);
        }
        if (!lock) {
            log.info("策略奖品库存加锁失败 {}" ,lockKey);
        }
        return lock;
    }

    @Override
    public void awardStockConsumeSendQueue(StrategyAwardStockKeyVO strategyAwardStockKeyVO) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUEUE_KEY;
        RBlockingQueue<StrategyAwardStockKeyVO> blockingQueue = redisService.getBlockingQueue(cacheKey);
        RDelayedQueue<StrategyAwardStockKeyVO> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        delayedQueue.offer(strategyAwardStockKeyVO, 3, TimeUnit.SECONDS);
    }

    @Override
    public StrategyAwardStockKeyVO takeQueueValue() {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUEUE_KEY;
        RBlockingQueue<StrategyAwardStockKeyVO> destinationQueue = redisService.getBlockingQueue(cacheKey);
        return destinationQueue.poll();
    }

    @Override
    public StrategyAwardStockKeyVO takeQueueValue(Long strategyId, Integer awardId) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUERY_KEY + Constants.UNDERLINE + strategyId + Constants.UNDERLINE + awardId;
        RBlockingQueue<StrategyAwardStockKeyVO> destinationQueue = redisService.getBlockingQueue(cacheKey);
        return destinationQueue.poll();
    }

    @Override
    public <K, V> Map<K, V> getMap(String key) {
        return redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key);
    }

    @Override
    public void cacheStrategyArmoryAlgorithm(String key, String beanName) {

        String cacheKey = Constants.RedisKey.STRATEGY_ARMORY_ALGORITHM_KEY + key;
        redisService.setValue(cacheKey, beanName);

    }

    @Override
    public String queryStrategyArmoryAlgorithmFromCache(String key) {
        String cacheKey = Constants.RedisKey.STRATEGY_ARMORY_ALGORITHM_KEY + key;
        if (!redisService.isExists(cacheKey)) return null;
        return redisService.getValue(cacheKey);
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        StrategyAwardPO strategyAwardPO = new StrategyAwardPO();
        strategyAwardPO.setStrategyId(strategyId);
        strategyAwardPO.setAwardId(awardId);
        strategyAwardDao.updateStrategyAwardStock(strategyAwardPO);
    }

    @Override
    public StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Integer awardId) {
        //优先从缓存获取
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + Constants.UNDERLINE + strategyId + Constants.UNDERLINE + awardId;
        StrategyAwardEntity strategyAwardEntity = redisService.getValue(cacheKey);
        if (null != strategyAwardEntity) return strategyAwardEntity;
        //查询数据
        StrategyAwardPO strategyAwardReq = new StrategyAwardPO();
        strategyAwardReq.setStrategyId(strategyId);
        strategyAwardReq.setAwardId(awardId);
        StrategyAwardPO strategyAwardRes = strategyAwardDao.queryStrategyAward(strategyAwardReq);
        //转换数据
        strategyAwardEntity = StrategyAwardEntity.builder()
                .strategyId(strategyAwardRes.getStrategyId())
                .awardId(strategyAwardRes.getAwardId())
                .awardTitle(strategyAwardRes.getAwardTitle())
                .awardSubtitle(strategyAwardRes.getAwardSubtitle())
                .awardCount(strategyAwardRes.getAwardCount())
                .awardCountSurplus(strategyAwardRes.getAwardCountSurplus())
                .awardRate(strategyAwardRes.getAwardRate())
                .sort(strategyAwardRes.getSort())
                .build();

        redisService.setValue(cacheKey, strategyAwardEntity);
        return strategyAwardEntity;
    }

    @Override
    public Long queryStrategyIdByActivityId(Long activityId) {
        return raffleActivityDao.queryStrategyIdByActivityId(activityId);
    }

    @Override
    public Integer queryTodayUserRaffleCount(String userId, Long strategyId) {
        //活动id
        Long activityId = raffleActivityDao.queryActivityIdByStrategyId(strategyId);
        //封装参数
        RaffleActivityAccountDayPO raffleActivityAccountDayReq = new RaffleActivityAccountDayPO();
        raffleActivityAccountDayReq.setUserId(userId);
        raffleActivityAccountDayReq.setActivityId(activityId);
        raffleActivityAccountDayReq.setDay(RaffleActivityAccountDayPO.currentDay());
        RaffleActivityAccountDayPO raffleActivityAccountDay = raffleActivityAccountDayDao.queryActivityAccountDayByUserId(raffleActivityAccountDayReq);
        if (null == raffleActivityAccountDay) return 0;
        //总次数 - 剩余的 = 今日参与的
        return raffleActivityAccountDay.getDayCount() - raffleActivityAccountDay.getDayCountSurplus();
    }

    @Override
    public Map<String, Integer> queryAwardRuleLockCount(String[] treeIds) {
        if (null == treeIds || treeIds.length == 0) return new HashMap<>();
        List<RuleTreeNodePO> ruleTreeNodes = ruleTreeNodeDao.queryRuleLocks(treeIds);
        HashMap<String, Integer> resultMap = new HashMap<>();
        for (RuleTreeNodePO ruleTreeNode : ruleTreeNodes) {
            String treeId = ruleTreeNode.getTreeId();
            Integer ruleValue = Integer.valueOf(ruleTreeNode.getRuleValue());
            resultMap.put(treeId, ruleValue);
        }
        return resultMap;
    }

    @Override
    public Integer queryActivityAccountTotalUseCount(String userId, Long strategyId) {
        Long activityId = raffleActivityDao.queryActivityIdByStrategyId(strategyId);
        RaffleActivityAccountPO raffleActivityAccountPO = raffleActivityAccountDao.queryActivityAccountByUserId(RaffleActivityAccountPO.builder()
                .userId(userId)
                .activityId(activityId)
                .build());
        return raffleActivityAccountPO.getTotalCount() - raffleActivityAccountPO.getTotalCountSurplus();
    }

    @Override
    public List<RuleWeightVO> queryAwardRuleWeightByStrategyId(Long strategyId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.STRATEGY_RULE_WEIGHT_KEY + strategyId;
        List<RuleWeightVO> ruleWeightVOS = redisService.getValue(cacheKey);
        if (null != ruleWeightVOS) return ruleWeightVOS;

        ruleWeightVOS = new ArrayList<>();
        // 1. 查询权重规则配置
        StrategyRulePO strategyRuleReq = new StrategyRulePO();
        strategyRuleReq.setStrategyId(strategyId);
        strategyRuleReq.setRuleModel(DefaultChainFactory.LogicModel.RULE_WEIGHT.getCode());
        String ruleValue = strategyRuleDao.queryStrategyRuleValue(strategyRuleReq);

        // 2. 借助实体对象转换规则
        StrategyRuleEntity strategyRuleEntity = new StrategyRuleEntity();
        strategyRuleEntity.setRuleModel(DefaultChainFactory.LogicModel.RULE_WEIGHT.getCode());
        strategyRuleEntity.setRuleValue(ruleValue);
        Map<String, List<Integer>> ruleWeightValues = strategyRuleEntity.getRuleWeightValues();

        // 3. 遍历规则组装奖品配置
        Set<String> ruleWeightKeys = ruleWeightValues.keySet();
        for (String ruleWeightKey : ruleWeightKeys) {
            List<Integer> awardIds = ruleWeightValues.get(ruleWeightKey);
            List<RuleWeightVO.Award> awardList = new ArrayList<>();
            // 也可以修改为一次从数据库查询
            for (Integer awardId : awardIds) {
                StrategyAwardPO strategyAwardReq = new StrategyAwardPO();
                strategyAwardReq.setStrategyId(strategyId);
                strategyAwardReq.setAwardId(awardId);
                StrategyAwardPO strategyAward = strategyAwardDao.queryStrategyAward(strategyAwardReq);
                awardList.add(RuleWeightVO.Award.builder()
                        .awardId(strategyAward.getAwardId())
                        .awardTitle(strategyAward.getAwardTitle())
                        .build());
            }

            ruleWeightVOS.add(RuleWeightVO.builder()
                    .ruleValue(ruleValue)
                    .weight(Integer.valueOf(ruleWeightKey.split(Constants.COLON)[0]))
                    .awardIds(awardIds)
                    .awardList(awardList)
                    .build());
        }

        // 设置缓存 - 实际场景中，这类数据，可以在活动下架的时候统一清空缓存。
        redisService.setValue(cacheKey, ruleWeightVOS);

        return ruleWeightVOS;
    }

    @Override
    public List<StrategyAwardStockKeyVO> queryOpenActivityStrategyAwardList() {
        List<StrategyAwardPO> strategyAwards = strategyAwardDao.queryOpenActivityStrategyAwardList();
        if (null == strategyAwards || strategyAwards.isEmpty()) return null;

        List<StrategyAwardStockKeyVO> strategyAwardStockKeyVOS = new ArrayList<>();
        for (StrategyAwardPO strategyAward: strategyAwards){
            StrategyAwardStockKeyVO strategyAwardStockKeyVO = StrategyAwardStockKeyVO.builder()
                    .strategyId(strategyAward.getStrategyId())
                    .awardId(strategyAward.getAwardId())
                    .build();
            strategyAwardStockKeyVOS.add(strategyAwardStockKeyVO);
        }

        return strategyAwardStockKeyVOS;
    }




}










