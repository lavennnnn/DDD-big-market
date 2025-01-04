package cn.hush.domain.strategy.service.armory.algorithm.impl;

import cn.hush.domain.strategy.model.entity.StrategyAwardEntity;
import cn.hush.domain.strategy.service.armory.algorithm.AbstractAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author Hush
 * @description
 * @create 2024-12-31 下午5:17
 */
@Component("oLogNAlgorithm")
@Slf4j
public class OLogNAlgorithm extends AbstractAlgorithm {

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public void armoryAlgorithm(String key, List<StrategyAwardEntity> strategyAwardEntities,
                                BigDecimal rateRange) {

        log.info("抽奖算法 OLog(n) 装配 key:{}", key);
        int from = 1;
        int to = 0;

        HashMap<Map<Integer, Integer>, Integer> table = new HashMap<>();
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            BigDecimal awardRate = strategyAwardEntity.getAwardRate();
            Integer awardId = strategyAwardEntity.getAwardId();

            to += awardRate.multiply(rateRange).intValue();

            Map<Integer, Integer> ft = new HashMap<>();
            ft.put(from, to);
            table.put(ft, awardId);
            from = to + 1;
        }

        repository.storeStrategyAwardSearchRateTables(key, to, table);

    }

    @Override
    public Integer dispatchAlgorithm(String key) {

        int rateRange = repository.getRateRange(key);
        Map<Map<String, Integer>, Integer> table = repository.getMap(key);

        if (table.size() <= 8) {
            log.info("抽奖算法 OLog(n) 抽奖计算（循环） key:{}", key);
            return forSearch(secureRandom.nextInt(rateRange), table);
        } else if (table.size() <= 16) {
            log.info("抽奖算法 OLog(n) 抽奖计算（二分） key:{}", key);
            return binarySearch(secureRandom.nextInt(rateRange), table);
        } else {
            log.info("抽奖算法 OLog(n) 抽奖计算（多线程） key:{}", key);
            return threadSearch(secureRandom.nextInt(rateRange), table);
        }
    }

    private Integer threadSearch(int randomNum, Map<Map<String, Integer>, Integer> table) {

        List<CompletableFuture<Map.Entry<Map<String, Integer>, Integer>>> futures = table.
                entrySet().stream()
                .map(entry -> CompletableFuture.supplyAsync(() -> {
                    Map<String, Integer> rangeMap = entry.getKey();
                    for (Map.Entry<String, Integer> rangeEntry : rangeMap.entrySet()) {
                        int start = Integer.parseInt(rangeEntry.getKey());
                        int end = rangeEntry.getValue();
                        if (randomNum >= start && randomNum <= end) {
                            return entry;
                        }
                    }
                    return null;
                }, threadPoolExecutor))
                .collect(Collectors.toList());

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.
                toArray(new CompletableFuture[0]));

        try {
            // 等待所有异步任务完成，同时返回第一个匹配的结果
            allFutures.join();
            for (CompletableFuture<Map.Entry<Map<String, Integer>, Integer>> future : futures) {
                Map.Entry<Map<String, Integer>, Integer> result = future.getNow(null);
                if (result != null) {
                    return result.getValue();
                }
            }
        } catch (CompletionException e) {
            e.printStackTrace();
        }

        return null;
    }


    // 二分查找法
    private Integer binarySearch(int randomNum, Map<Map<String, Integer>, Integer> table) {

        List<Map.Entry<Map<String, Integer>, Integer>> entries = new ArrayList<>(table.entrySet());

        // List 中的元素按 String 的字面大小排序
        entries.sort(Comparator.
                comparingInt(e -> Integer.parseInt(e.getKey().keySet().iterator().next())));
        
        int left = 0, right = entries.size() - 1;

        while (left <= right) {
            int mid = (left + right) / 2;
            Map.Entry<Map<String, Integer>, Integer> entry = entries.get(mid);
            Map<String, Integer> rangeMap = entry.getKey();
            Map.Entry<String, Integer> range = rangeMap.entrySet().iterator().next();

            int start = Integer.parseInt(range.getKey());
            int end = range.getValue();

            if (randomNum < start ) {
                right = mid - 1;
            } else if (randomNum > end) {
                left = mid + 1;
            } else {
                return entry.getValue();
            }


        }
        return null;

    }

    //循环查找法
    private Integer forSearch(int randomNum, Map<Map<String, Integer>, Integer> table) {

        Integer awardId = null;

        for (Map.Entry<Map<String, Integer>, Integer> entry : table.entrySet()) {
            Map<String, Integer> rangeMap = entry.getKey();

            for (Map.Entry<String, Integer> rangeEntry : rangeMap.entrySet()) {
                int start = Integer.parseInt(rangeEntry.getKey());
                int end = rangeEntry.getValue();

                if (randomNum >= start && randomNum <= end) {
                    awardId = entry.getValue();
                    break;
                }
            }
            if (awardId != null) {
                break;
            }
        }
        return awardId;
    }
}
