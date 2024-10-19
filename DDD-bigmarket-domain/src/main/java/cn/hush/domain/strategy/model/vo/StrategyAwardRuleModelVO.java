package cn.hush.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hush
 * @description 抽奖策略规则值对象；值对象，没有唯一ID，仅限于从数据库查询对象
 * @create 2024-10-12 上午1:22
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardRuleModelVO {

    private String ruleModels;


}
