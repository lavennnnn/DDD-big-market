package cn.hush.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * 奖品表
 */
@Data
public class AwardPO {

    //自增id
    private Long id;
    //抽奖奖品id - 内部流转使用
    private Long awardId;
    //奖品对接标识
    private String awardKey;
    //奖品配置信息
    private String awardConfig;
    //奖品内容描述
    private String awardDesc;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;

}
