package cn.hush.domain.strategy.model.entity;

import cn.hush.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import lombok.*;

/**
 * @author Hush
 * @description 规则动作实体
 * @create 2024-10-09 下午10:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleActionEntity <T extends RuleActionEntity.RaffleEntity> {

    private String code = RuleLogicCheckTypeVO.ALLOW.getCode();

    private String info = RuleLogicCheckTypeVO.ALLOW.getInfo();

    private String ruleModel;

    private T data;


    static public class RaffleEntity {



    }

    @EqualsAndHashCode(callSuper=true)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static public class RaffleBeforeEntity extends RaffleEntity {
        //策略id
        private Long strategyId;
        //权重值key：用于抽奖时，选择权重抽奖
        private String ruleWeightValueKey;
        //奖品id
        private Integer awardId;
    }

    static public class RaffleDuringEntity extends RaffleEntity {

    }

    static public class RaffleAfterEntity extends RaffleEntity {

    }
}
