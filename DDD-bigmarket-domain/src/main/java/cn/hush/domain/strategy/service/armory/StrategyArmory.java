package cn.hush.domain.strategy.service.armory;

import cn.hush.domain.strategy.model.entity.StrategyAwardEntity;
import cn.hush.domain.strategy.repository.IStrategyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author Hush
 * @description 策略装配（兵工厂），负责初始化策略计算
 * @create 2024-08-28 下午8:51
 */

@Slf4j
@Service
public class StrategyArmory implements IStrategyArmory{

    @Resource
    private IStrategyRepository repository;

    // 装配抽奖策略
    @Override
    public void assembleLotteryStrategy(Long strategyId) {

        //1. 查询策略配置
        List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);
        //if (strategyAwardEntities == null || strategyAwardEntities.isEmpty()) return;

        //2. 获取最小概率值
        BigDecimal minAwardRate = strategyAwardEntities.stream().
                map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        //3. 获取概率值的总和
        BigDecimal totalAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //4. 用 1 % 0.0001 获取概率范围
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);

        //5. 生成策略奖品概率查找表「这里指需要在list集合中，存放上对应的奖品占位即可，占位越多等于概率越高」
        ArrayList<Integer> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());

        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Integer awardId = strategyAwardEntity.getAwardId();
            BigDecimal awardRate = strategyAwardEntity.getAwardRate();

            //计算每个概率值需要存放到查找表的数量，循环填充
            for (int i = 0; i < rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue(); i++) {
                strategyAwardSearchRateTables.add(awardId);
            }
        }

        //6. 对存储的奖品进行乱序操作
        Collections.shuffle(strategyAwardSearchRateTables);

        //7. 生成出Map集合，key值，对应的就是后续的概率值。通过概率来获得对应的奖品ID
        HashMap<Integer, Integer> shuffledStrategyAwardSearchRateTables = new HashMap<>();
        for (int i = 0; i < strategyAwardSearchRateTables.size(); i++) {
            shuffledStrategyAwardSearchRateTables.put(i, strategyAwardSearchRateTables.get(i));
        }

        //8. 存储到redis
        //todo
        repository.storeStrategyAwardSearchRateTables(strategyId, BigDecimal.valueOf(shuffledStrategyAwardSearchRateTables.size()), shuffledStrategyAwardSearchRateTables);
    }

    @Override
    public Integer getRandomAward(Long strategyId) {
        // 分布式部署下，不一定为当前应用做的策略装配。也就是值不一定会保存到本应用，而是分布式应用，所以需要从 Redis 中获取。
        BigDecimal rangeRate = repository.getRateRange(strategyId);
        // 通过生成的随机值，获取概率值奖品查找表的结果
        return repository.getStrategyAwardAssemble(strategyId, new SecureRandom().nextInt(rangeRate.intValue()));
    }
}
