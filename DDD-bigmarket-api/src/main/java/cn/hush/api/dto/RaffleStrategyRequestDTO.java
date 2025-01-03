package cn.hush.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Hush
 * @description 抽奖 请求对象
 * @create 2024-11-06 下午5:36
 */
@Data
public class RaffleStrategyRequestDTO implements Serializable {

    // 抽奖策略ID
    private Long strategyId;


}
