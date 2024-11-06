package cn.hush.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Hush
 * @description 抽奖应答结果
 * @create 2024-11-06 下午5:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleResponseDTO {

    // 奖品ID
    private Integer awardId;
    // 排序编号【策略奖品配置的奖品顺序编号】
    private Integer awardIndex;


}
