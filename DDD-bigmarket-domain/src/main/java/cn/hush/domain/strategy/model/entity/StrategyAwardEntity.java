package cn.hush.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Hush
 * @description 策略奖品实体
 * @create 2024-08-28 下午11:58
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyAwardEntity {

    //抽奖策略ID
    private Long strategyId;
    //抽奖奖品ID - 内部流转使用
    private Integer awardId;
    //奖品标题
    private String awardTitle;
    //奖品副标题
    private String awardSubtitle;
    //奖品库存总量
    private Integer awardCount;
    //奖品库存剩余
    private Integer awardCountSurplus;
    //奖品中奖概率
    private BigDecimal awardRate;
    //排序
    private Integer sort;

}
