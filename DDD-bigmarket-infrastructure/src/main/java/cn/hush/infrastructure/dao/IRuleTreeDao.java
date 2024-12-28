package cn.hush.infrastructure.dao;
import cn.hush.infrastructure.dao.po.RuleTreePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Hush
 * @description 规则树表DAO
 * @create 2024-10-18 上午9:26
 */
@Mapper
public interface IRuleTreeDao {

    RuleTreePO queryRuleTreePOByTreeId(String treeId);

}
