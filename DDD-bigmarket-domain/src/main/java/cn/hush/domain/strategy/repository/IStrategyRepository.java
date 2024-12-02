package cn.hush.domain.strategy.repository;

import cn.hush.domain.strategy.model.entity.StrategyAwardEntity;
import cn.hush.domain.strategy.model.entity.StrategyEntity;
import cn.hush.domain.strategy.model.entity.StrategyRuleEntity;
import cn.hush.domain.strategy.model.vo.RuleTreeVO;
import cn.hush.domain.strategy.model.vo.StrategyAwardRuleModelVO;
import cn.hush.domain.strategy.model.vo.StrategyAwardStockKeyVO;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Hush
 * @description 策略仓储接口
 * @create 2024-08-28 下午8:52
 */
public interface IStrategyRepository {

    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchRateTables(String key, Integer rateRange, Map<Integer, Integer> shuffledStrategyAwardSearchRateTables);

    int getRateRange(Long strategyId);

    int getRateRange(String key);

    Integer getStrategyAwardAssemble(String key, Integer rateKey);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, String ruleModel);

    StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId);

    RuleTreeVO queryRuleTreeVOByTreeId(String treeLock);

    /**
     * 缓存奖品库存
     * @param cacheKey key
     * @param awardCount 库存值
     */
    void cacheStrategyAwardCount(String cacheKey, Integer awardCount);

    Boolean subtractAwardStock(String cacheKey);

    Boolean subtractAwardStock(String cacheKey, Date endDateTime);

    void awardStockConsumeSendQueue(StrategyAwardStockKeyVO strategyAwardStockKeyVO);

    StrategyAwardStockKeyVO takeQueueValue();

    void updateStrategyAwardStock(Long strategyId, Integer awardId);

    StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Integer awardId);

    Long queryStrategyIdByActivityId(Long activityId);

    Integer queryTodayUserRaffleCount(String userId, Long strategyId);

    Map<String, Integer> queryAwardRuleLockCount(String[] treeIds);
}










