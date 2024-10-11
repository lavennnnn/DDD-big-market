package cn.hush.domain.strategy.model.entity;

import cn.hush.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Hush
 * @description 策略实体
 * @create 2024-09-02 下午6:04
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StrategyEntity {

    //抽奖策略id
    private Long strategyId;
    //抽奖策略描述
    private String strategyDesc;
    //策略规则模型
    private String ruleModels;

    public String[] ruleModels() {
        if (StringUtils.isBlank(ruleModels)) return null;
        return ruleModels.split(Constants.SPLIT);
    }

    public String getRuleWeight() {
        String[] ruleModels = this.ruleModels();
        if (null == ruleModels) return null;
        for (String ruleModel : ruleModels) {
            if ("rule_weight".equals(ruleModel)) return ruleModel;
        }
        return null;
    }

}
