package cn.hush.api.dto;

import lombok.Data;

/**
 * @author Hush
 * @description 抽奖奖品列表，请求对象
 * @create 2024-11-06 下午5:12
 */
@Data
public class RaffleAwardListRequestDTO {

    // 抽奖策略ID
    private Long strategyId;

}