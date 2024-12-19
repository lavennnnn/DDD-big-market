package cn.hush.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Hush
 * @description 抽奖奖品列表，响应对象
 * @create 2024-11-06 下午5:31
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RaffleAwardListResponseDTO implements Serializable {

    // 奖品ID
    private Integer awardId;
    // 奖品标题
    private String awardTitle;
    // 奖品副标题【抽奖1次后解锁】
    private String awardSubtitle;
    // 排序编号
    private Integer sort;
    // 奖品次数规则 - 抽奖 n 次后解锁，未配置则为空
    private Integer awardRuleLockCount;
    // 奖品是否解锁 - true 已解锁、false 未解锁
    private Boolean isAwardUnlocked;
    // 等待解锁次数 = 规定的抽奖次数 n - 用户已经抽奖的次数
    private Integer waitUnlockCount;

}
