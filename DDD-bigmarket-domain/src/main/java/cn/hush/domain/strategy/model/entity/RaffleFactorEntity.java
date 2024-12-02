package cn.hush.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Hush
 * @description 抽奖因子实体
 * @create 2024-10-09 下午9:52
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleFactorEntity {

    private String userId;
    private Long strategyId;
    //活动结束时间
    private Date endDateTime;
}
