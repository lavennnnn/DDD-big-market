package cn.hush.domain.award.adapter.port;

/**
 * @author Hush
 * @description 奖品对接接口
 * @create 2025-01-10 上午12:58
 */
public interface IAwardPort {

    void adjustAmount(String userId, Integer increaseQuota) throws Exception;

}
