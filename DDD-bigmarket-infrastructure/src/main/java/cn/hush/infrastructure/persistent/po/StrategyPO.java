package cn.hush.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * 抽奖策略
 */
@Data
public class StrategyPO {

    //自增id
    private Long id;
    //抽奖策略id
    private Long strategyId;
    //抽奖策略描述
    private String strategyDesc;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
}
