package cn.hush.trigger.http;

import cn.hush.api.IRaffleService;
import cn.hush.api.dto.RaffleAwardListRequestDTO;
import cn.hush.api.dto.RaffleAwardListResponseDTO;
import cn.hush.api.dto.RaffleRequestDTO;
import cn.hush.api.dto.RaffleResponseDTO;
import cn.hush.domain.strategy.model.entity.RaffleAwardEntity;
import cn.hush.domain.strategy.model.entity.RaffleFactorEntity;
import cn.hush.domain.strategy.model.entity.StrategyAwardEntity;
import cn.hush.domain.strategy.service.IRaffleAward;
import cn.hush.domain.strategy.service.IRaffleStrategy;
import cn.hush.domain.strategy.service.armory.IStrategyArmory;
import cn.hush.types.enums.ResponseCode;
import cn.hush.types.exception.AppException;
import cn.hush.types.model.Response;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hush
 * @description 抽奖服务
 * @create 2024-11-06 下午5:40
 */
@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/raffle/")
public class IRaffleController implements IRaffleService {

    @Resource
    private IStrategyArmory strategyArmory;

    @Resource
    private IRaffleAward raffleAward;

    @Resource
    private IRaffleStrategy raffleStrategy;
    /**
     * 策略装配，将策略信息装配到缓存中
     * <a href="http://localhost:8091/api/v1/raffle/strategy_armory">/api/v1/raffle/strategy_armory</a>
     *
     * @param strategyId 策略ID
     * @return 装配结果
     */
    @RequestMapping(value = "strategy_armory", method = RequestMethod.GET)
    @Override
    public Response<Boolean> strategyArmory(Long strategyId) {
        try{
            log.info("抽奖策略装配开始 strategyId:{}", strategyId);
            boolean armoryStatus = strategyArmory.assembleLotteryStrategy(strategyId);
            Response<Boolean> response = Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(armoryStatus)
                    .build();
            log.info("抽奖策略装配完成 strategyId: {} response: {}", strategyId, JSON.toJSON(response));
            return response;
        }catch (Exception e) {
            log.error("抽奖策略装配失败 strategyId: {}", strategyId, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }

    }

    /**
     * 查询奖品列表
     * <a href="http://localhost:8091/api/v1/raffle/query_raffle_award_list">/api/v1/raffle/query_raffle_award_list</a>
     * 请求参数 raw json
     *
     * @param requestDTO {"strategyId":1000001}
     * @return 奖品列表
     */
    @RequestMapping(value = "query_raffle_award_list", method = RequestMethod.POST)
    @Override
    public Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(@RequestBody RaffleAwardListRequestDTO requestDTO) {
        try{
            log.info("查询奖品列表开始 strategyId:{}", requestDTO.getStrategyId());
            //查询奖品配置
            List<StrategyAwardEntity> strategyAwardEntityList = raffleAward.queryRaffleStrategyAwardList(requestDTO.getStrategyId());
            ArrayList<RaffleAwardListResponseDTO> raffleAwardListResponseDTOS = new ArrayList<>(strategyAwardEntityList.size());
            for (StrategyAwardEntity strategyAward : strategyAwardEntityList) {
                raffleAwardListResponseDTOS.add(RaffleAwardListResponseDTO.builder()
                        .awardId(strategyAward.getAwardId())
                        .awardTitle(strategyAward.getAwardTitle())
                        .awardSubtitle(strategyAward.getAwardSubtitle())
                        .sort(strategyAward.getSort())
                        .build());
            }
            Response<List<RaffleAwardListResponseDTO>> response = Response.<List<RaffleAwardListResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(raffleAwardListResponseDTOS).build();

            log.info("查询抽奖奖品列表配置完成 strategyId: {} response: {}", requestDTO.getStrategyId(), JSON.toJSON(response));
            return response;
        }catch(Exception e){
            log.error("查询抽奖奖品列表失败 strategyId: {}", requestDTO.getStrategyId(), e);
            return Response.<List<RaffleAwardListResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();

        }
    }

    /**
     * 随机抽奖接口
     * <a href="http://localhost:8091/api/v1/raffle/random_raffle">/api/v1/raffle/random_raffle</a>
     *
     * @param requestDTO 请求参数 {"strategyId":1000001}
     * @return 抽奖结果
     */
    @Override
    @RequestMapping(value = "random_raffle", method = RequestMethod.POST)
    public Response<RaffleResponseDTO> randomRaffle(@RequestBody RaffleRequestDTO requestDTO) {
        try {
            log.info("随机抽奖开始 strategyId: {}", requestDTO.getStrategyId());
            // 调用抽奖接口
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(RaffleFactorEntity.builder()
                    .userId("system")
                    .strategyId(requestDTO.getStrategyId())
                    .build());
            // 封装返回结果
            Response<RaffleResponseDTO> response = Response.<RaffleResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(RaffleResponseDTO.builder()
                            .awardId(raffleAwardEntity.getAwardId())
                            .awardIndex(raffleAwardEntity.getSort())
                            .build())
                    .build();
            log.info("随机抽奖完成 strategyId: {} response: {}", requestDTO.getStrategyId(), JSON.toJSONString(response));
            return response;
        } catch (AppException e) {
            log.error("随机抽奖失败 strategyId：{} {}", requestDTO.getStrategyId(), e.getInfo());
            return Response.<RaffleResponseDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("随机抽奖失败 strategyId：{}", requestDTO.getStrategyId(), e);
            return Response.<RaffleResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }

    }
}













