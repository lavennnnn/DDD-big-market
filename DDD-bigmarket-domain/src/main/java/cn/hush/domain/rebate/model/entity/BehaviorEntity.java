package cn.hush.domain.rebate.model.entity;

import cn.hush.domain.rebate.model.valobj.BehaviorTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Hush
 * @description 返利行为实体
 * @create 2024-12-04 下午8:47
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BehaviorEntity {

    /** 用户ID */
    private String userId;

    /** 行为类型（sign_in 签到、openai_pay 支付） */
    private BehaviorTypeVO behaviorType;

    /** 业务ID - 拼接的唯一值 */
    private String outBusinessNo;
}
