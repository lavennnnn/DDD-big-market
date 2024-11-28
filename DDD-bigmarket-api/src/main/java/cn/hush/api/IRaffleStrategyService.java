package cn.hush.api;

import cn.hush.api.dto.RaffleAwardListRequestDTO;
import cn.hush.api.dto.RaffleAwardListResponseDTO;
import cn.hush.api.dto.RaffleStrategyRequestDTO;
import cn.hush.api.dto.RaffleStrategyResponseDTO;
import cn.hush.types.model.Response;

import java.util.List;

/**
 * @author Hush
 * @description 抽奖服务接口
 * @create 2024-11-06 下午5:09
 */
public interface IRaffleStrategyService {

    /**
     * 策略装配接口
     * @param strategyId 策略id
     * @return 装配结果
     */
    Response<Boolean> strategyArmory(Long strategyId);

    /**
     * 查询抽奖奖品列表配置
     *
     * @param requestDTO 抽奖奖品列表查询请求参数
     * @return 奖品列表数据
     */
    Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(RaffleAwardListRequestDTO requestDTO);

    /**
     * 随机抽奖接口
     *
     * @param requestDTO 请求参数
     * @return 抽奖结果
     */
    Response<RaffleStrategyResponseDTO> randomRaffle(RaffleStrategyRequestDTO requestDTO);


}
