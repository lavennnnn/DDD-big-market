package cn.hush.infrastructure.dao.po;

import lombok.Data;

import java.util.Date;

/**
 * @author Hush
 * @description 规则树
 * @create 2024-10-18 上午8:03
 */
@Data
public class RuleTreePO {

    /** 自增ID */
    private Long id;
    /** 规则树ID */
    private String treeId;
    /** 规则树名称 */
    private String treeName;
    /** 规则树描述 */
    private String treeDesc;
    /** 规则根节点 */
    private String treeRootRuleKey;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;


}
