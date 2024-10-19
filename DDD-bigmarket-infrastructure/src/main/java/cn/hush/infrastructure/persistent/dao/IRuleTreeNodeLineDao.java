package cn.hush.infrastructure.persistent.dao;

import cn.hush.infrastructure.persistent.po.RuleTreeNodeLinePO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Hush
 * @description 规则树节点线DAO
 * @create 2024-10-18 上午9:27
 */
@Mapper
public interface IRuleTreeNodeLineDao {

    List<RuleTreeNodeLinePO> queryRuleTreeNodeLinePOListByTreeId(String treeId);

}
