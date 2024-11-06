package cn.hush.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Hush
 * @description 抽奖奖品实体
 * @create 2024-10-09 下午9:54
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleAwardEntity {

    /** 奖品ID */
    private Integer awardId;
    /** 奖品配置信息 */
    private String awardConfig;
    /** 排序 */
    private Integer sort;

}
