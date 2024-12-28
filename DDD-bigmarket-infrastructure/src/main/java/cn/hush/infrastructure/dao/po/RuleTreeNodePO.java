package cn.hush.infrastructure.dao.po;

import lombok.Data;

import java.util.Date;

/**
 * @author Hush
 * @description 规则树节点
 * @create 2024-10-18 上午8:03
 */
@Data
public class RuleTreeNodePO {

    /** 自增ID */
    private Long id;
    /** 规则树ID */
    private String treeId;
    /** 规则Key */
    private String ruleKey;
    /** 规则描述 */
    private String ruleDesc;
    /** 规则值 */
    private String ruleValue;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;


}
