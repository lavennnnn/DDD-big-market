package cn.hush.trigger.http;

import cn.hush.api.IRaffleActivityService;
import cn.hush.api.dto.*;
import cn.hush.domain.activity.model.entity.*;
import cn.hush.domain.activity.model.vo.OrderTradeTypeVO;
import cn.hush.domain.activity.service.IRaffleActivityAccountQuotaService;
import cn.hush.domain.activity.service.IRaffleActivityPartakeService;
import cn.hush.domain.activity.service.armory.IActivityArmory;
import cn.hush.domain.activity.service.product.RaffleActivitySkuProductService;
import cn.hush.domain.award.model.entity.UserAwardRecordEntity;
import cn.hush.domain.award.model.vo.AwardStateVO;
import cn.hush.domain.award.service.IAwardService;
import cn.hush.domain.credit.model.entity.CreditAccountEntity;
import cn.hush.domain.credit.model.entity.TradeEntity;
import cn.hush.domain.credit.model.valobj.TradeNameVO;
import cn.hush.domain.credit.model.valobj.TradeTypeVO;
import cn.hush.domain.credit.service.adjust.CreditAdjustService;
import cn.hush.domain.rebate.model.entity.BehaviorEntity;
import cn.hush.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import cn.hush.domain.rebate.model.valobj.BehaviorTypeVO;
import cn.hush.domain.rebate.service.IBehaviorRebateService;
import cn.hush.domain.strategy.model.entity.RaffleAwardEntity;
import cn.hush.domain.strategy.model.entity.RaffleFactorEntity;
import cn.hush.domain.strategy.service.IRaffleStrategy;
import cn.hush.domain.strategy.service.armory.IStrategyArmory;
import cn.hush.types.annotations.DCCValue;
import cn.hush.types.annotations.RateLimiterAccessInterceptor;
import cn.hush.types.enums.ResponseCode;
import cn.hush.types.exception.AppException;
import cn.hush.api.response.Response;
import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Hush
 * @description 抽奖活动服务
 * @create 2024-11-27 下午6:23
 */
@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/raffle/activity")
@DubboService(version = "1.0")
public class  RaffleActivityController implements IRaffleActivityService {

    private final SimpleDateFormat sdfDay = new SimpleDateFormat("yyyyMMdd");

    @Resource
    private IActivityArmory activityArmory;
    @Resource
    private IStrategyArmory strategyArmory;
    @Resource
    private IRaffleActivityPartakeService raffleActivityPartakeService;
    @Resource
    private IRaffleStrategy raffleStrategy;
    @Resource
    private IAwardService awardService;
    @Resource
    private IBehaviorRebateService behaviorRebateService;
    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;
    @Resource
    private CreditAdjustService creditAdjustService;
    @Resource
    private RaffleActivitySkuProductService raffleActivitySkuProductService;

    // dcc 统一配置中心动态配置降级开关
    @DCCValue("degradeSwitch:open")
    private String degradeSwitch;

    /**
     * 活动装配 - 数据预热 | 把活动配置的对应的 sku 一起装配
     *
     * @param activityId 活动ID
     * @return 装配结果
     * <p>
     * 接口：<a href="http://localhost:8091/api/v1/raffle/activity/armory">/api/v1/raffle/activity/armory</a>
     * 入参：{"activityId":100001,"userId":"xiaofuge"}
     * curl --request GET \
     *   --url '<a href="http://localhost:8091/api/v1/raffle/activity/armory?activityId=100301">...</a>'
     */

    @RequestMapping(value = "armory" ,method = RequestMethod.GET)
    @Override
    public Response<Boolean> armory(@RequestParam Long activityId) {
        try {
            log.info("活动装配，数据预热，开始 activityId:{}", activityId);
            // 1. 活动装配
            activityArmory.assembleActivitySkuByActivityId(activityId);
            //2. 策略装配
            strategyArmory.assembleLotteryStrategyByActivityId(activityId);
            Response<Boolean> response = Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(true)
                    .build();
            log.info("活动装配，数据预热，完成 activityId:{}", activityId);
            return response;
        } catch (Exception e) {
            log.error("活动装配，数据预热，失败 activityId:{}", activityId, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }

    }


    /**
     * 抽奖接口
     *
     * @param request 请求对象
     * @return 抽奖结果
     * <p>
     * 接口：<a href="http://localhost:8091/api/v1/raffle/activity/draw">/api/v1/raffle/activity/draw</a>
     * 入参：{"activityId":100001,"userId":"xiaofuge"}
     * curl --request POST \
     *   --url http://localhost:8091/api/v1/raffle/activity/draw \
     *   --header 'content-type: application/json' \
     *   --data '{
     *     "userId":"xiaofuge",
     *     "activityId": 100301
     * }'
     */
    @RateLimiterAccessInterceptor(key = "userId", fallbackMethod = "drawRateLimiterError", permitsPerSecond = 1.0d, blackListCount = 1)
    @RequestMapping(value = "draw", method = RequestMethod.POST)
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "150")
    }, fallbackMethod = "drawHystrixError"
    )
    @Override
    public Response<ActivityDrawResponseDTO> draw(@RequestBody ActivityDrawRequestDTO request) {
        try {
            log.info("活动抽奖 userId:{} activityId:{}", request.getUserId(), request.getActivityId());

            // 降级判断
            if (StringUtils.isNotBlank(degradeSwitch) && "close".equals(degradeSwitch)) {
                return Response.<ActivityDrawResponseDTO>builder()
                        .code(ResponseCode.DEGRADE_SWITCH.getCode())
                        .info(ResponseCode.DEGRADE_SWITCH.getInfo())
                        .build();
            }

            // 1. 参数校验
            if (StringUtils.isBlank(request.getUserId()) || null == request.getActivityId()) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }

            // 2. 参与活动 - 创建参与记录订单
            UserRaffleOrderEntity orderEntity = raffleActivityPartakeService.createOrder(request.getUserId(), request.getActivityId());
            log.info("活动抽奖，创建订单 userId:{} activityId:{} orderId:{}", request.getUserId(), request.getActivityId(), orderEntity.getOrderId());

            // 3. 抽奖策略 - 执行抽奖
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(RaffleFactorEntity.builder()
                    .userId(orderEntity.getUserId())
                    .strategyId(orderEntity.getStrategyId())
                    .endDateTime(orderEntity.getEndDateTime())
                    .build());

            // 4. 存放结果 - 写入中奖记录
            UserAwardRecordEntity userAwardRecord = UserAwardRecordEntity.builder()
                    .userId(orderEntity.getUserId())
                    .activityId(orderEntity.getActivityId())
                    .strategyId(orderEntity.getStrategyId())
                    .orderId(orderEntity.getOrderId())
                    .awardId(raffleAwardEntity.getAwardId())
                    .awardTitle(raffleAwardEntity.getAwardTitle())
                    .awardTime(new Date())
                    .awardState(AwardStateVO.create)
                    .awardConfig(raffleAwardEntity.getAwardConfig())
                    .build();
            awardService.saveUserAwardRecord(userAwardRecord);

            // 5. 返回结果
            return Response.<ActivityDrawResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(ActivityDrawResponseDTO.builder()
                            .awardId(raffleAwardEntity.getAwardId())
                            .awardTitle(raffleAwardEntity.getAwardTitle())
                            .awardIndex(raffleAwardEntity.getSort())
                            .build())
                    .build();

        } catch (AppException e) {
            log.error("活动抽奖失败 userId:{} activityId:{}", request.getUserId(), request.getActivityId(), e);
            return Response.<ActivityDrawResponseDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("活动抽奖失败 userId:{} activityId:{}", request.getUserId(), request.getActivityId(), e);
            return Response.<ActivityDrawResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();

        }
    }

    public Response<ActivityDrawResponseDTO> drawRateLimiterError(@RequestBody ActivityDrawRequestDTO request) {
        log.info("活动抽奖限流 userId:{} activityId:{}", request.getUserId(), request.getActivityId());
        return Response.<ActivityDrawResponseDTO>builder()
                .code(ResponseCode.RATE_LIMITER.getCode())
                .info(ResponseCode.RATE_LIMITER.getInfo())
                .build();
    }


    public Response<ActivityDrawResponseDTO> drawHystrixError(@RequestBody ActivityDrawRequestDTO request) {

        log.info("活动抽奖熔断 userId:{} activityId:{}", request.getUserId(), request.getActivityId());
        return Response.<ActivityDrawResponseDTO>builder()
                .code(ResponseCode.HYSTRIX.getCode())
                .info(ResponseCode.HYSTRIX.getInfo())
                .build();

    }

    /**
     * 日历签到返利接口
     * <a href="http://localhost:8091/api/v1/raffle/activity/calender_sign_in_rebate">...</a>
     *
     * @param userId 用户id
     * @return 结果信息
     */
    @RequestMapping(value = "calender_sign_in_rebate", method = RequestMethod.POST)
    @Override
    public Response<Boolean> calenderSignInRebate(@RequestParam String userId) {
        try {
            log.info("日历签到返利开始 userId:{}" ,userId);
            BehaviorEntity behaviorEntity = BehaviorEntity.builder()
                    .userId(userId)
                    .behaviorType(BehaviorTypeVO.SIGN_IN)
                    .outBusinessNo(sdfDay.format(new Date()))
                    .build();
            List<String> orderList = behaviorRebateService.createOrder(behaviorEntity);
            log.info("签到返利完成 userId:{} orderId:{}", userId, JSON.toJSONString(orderList));
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(true)
                    .build();
        }catch (AppException e) {
            log.error("签到返利异常 userId:{}", userId, e);
            return Response.<Boolean>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        }
        catch (Exception e) {
            log.error("签到返利失败 userId:{}", userId, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @RequestMapping(value = "is_calender_sign_in_rebate_done", method = RequestMethod.POST)
    @Override
    public Response<Boolean> isCalenderSignInRebateDone(@RequestParam String userId) {
        try {
            log.info("查询用户是否完成日历签到返利 开始…… userId:{}" ,userId);
            String outBusinessNo = sdfDay.format(new Date());
            List<BehaviorRebateOrderEntity> behaviorRebateOrderEntities = behaviorRebateService.queryOrderByUserIdAndOutBusinessNo(userId, outBusinessNo);
            log.info("查询用户是否完成日历签到返利 完成 userId:{} orders.size:{}" ,userId, behaviorRebateOrderEntities.size());
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(!behaviorRebateOrderEntities.isEmpty())
                    .build();
        }catch (Exception e) {
            log.error("查询用户是否完成日历签到返利 失败！ userId:{}", userId, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }

    }

    @RequestMapping(value = "query_user_activity_account", method = RequestMethod.POST)
    @Override
    public Response<UserActivityAccountResponseDTO> queryUserActivityAccount(@RequestBody UserActivityAccountRequestDTO request) {
        try {
            log.info("查询用户活动账户 开始…… userId:{}， activityId:{}", request.getUserId(), request.getActivityId());
            // 1. 参数校验
            if (StringUtils.isBlank(request.getUserId()) || null == request.getActivityId()) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }
            ActivityAccountEntity activityAccountEntity = raffleActivityAccountQuotaService.queryActivityAccountEntity(request.getActivityId(), request.getUserId());
            UserActivityAccountResponseDTO userActivityAccountResponseDTO = UserActivityAccountResponseDTO.builder()
                    .totalCount(activityAccountEntity.getTotalCount())
                    .totalCountSurplus(activityAccountEntity.getTotalCountSurplus())
                    .dayCount(activityAccountEntity.getDayCount())
                    .dayCountSurplus(activityAccountEntity.getDayCountSurplus())
                    .monthCount(activityAccountEntity.getMonthCount())
                    .monthCountSurplus(activityAccountEntity.getMonthCountSurplus())
                    .build();
            log.info("查询用户活动账户 完成! userId:{} activityId:{} dto:{}", request.getUserId(), request.getActivityId(), JSON.toJSONString(userActivityAccountResponseDTO));
            return Response.<UserActivityAccountResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(userActivityAccountResponseDTO)
                    .build();

        }catch (Exception e){
            log.error("查询用户活动账户失败 userId:{} activityId:{}", request.getUserId(), request.getActivityId(), e);
            return Response.<UserActivityAccountResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }

    }

    @RequestMapping(value = "query_sku_product_list_by_activity_id", method = RequestMethod.POST)
    @Override
    public Response<List<SkuProductResponseDTO>> querySkuProductListByActivityId(Long activityId) {
        try {
            log.info("查询sku商品集合开始 activityId:{}", activityId);
            // 1. 参数校验
            if (null == activityId) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(),ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }
            // 2. 查询商品 & 封装数据
            List<SkuProductEntity> skuProductEntities = raffleActivitySkuProductService.querySkuProductEntityListByActivityId(activityId);
            ArrayList<SkuProductResponseDTO> skuProductResponseDTOS = new ArrayList<>(skuProductEntities.size());
            for (SkuProductEntity skuProductEntity : skuProductEntities) {
                SkuProductResponseDTO.ActivityCount activityCount = new SkuProductResponseDTO.ActivityCount();
                activityCount.setTotalCount(skuProductEntity.getActivityCount().getTotalCount());
                activityCount.setMonthCount(skuProductEntity.getActivityCount().getMonthCount());
                activityCount.setDayCount(skuProductEntity.getActivityCount().getDayCount());

                SkuProductResponseDTO skuProductResponseDTO = new SkuProductResponseDTO();
                skuProductResponseDTO.setActivityId(skuProductEntity.getActivityId());
                skuProductResponseDTO.setSku(skuProductEntity.getSku());
                skuProductResponseDTO.setActivityCountId(skuProductEntity.getActivityCountId());
                skuProductResponseDTO.setActivityCount(activityCount);
                skuProductResponseDTO.setProductAmount(skuProductEntity.getProductPriceAmount());
                skuProductResponseDTO.setStockCount(skuProductEntity.getStockCount());
                skuProductResponseDTO.setStockCountSurplus(skuProductEntity.getStockCountSurplus());

                skuProductResponseDTOS.add(skuProductResponseDTO);
            }
            log.info("查询sku商品集合完成 activityId:{} skuProductResponseDTOS:{}", activityId, JSON.toJSONString(skuProductResponseDTOS));
            return Response.<List<SkuProductResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(skuProductResponseDTOS)
                    .build();

        }catch (Exception e){
            log.error("查询sku商品集合失败 activityId:{}", activityId, e);
            return Response.<List<SkuProductResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "query_user_credit_account", method = RequestMethod.POST)
    @Override
    public Response<BigDecimal> queryUserCreditAccount(String userId) {
        try {
            log.info("查询用户积分值开始 userId:{}", userId);
            CreditAccountEntity creditAccountEntity =  creditAdjustService.queryUserCreditAccount(userId);
            log.info("查询用户积分值完成 userId:{} adjustAmount:{}", userId, creditAccountEntity.getAdjustAmount());
            return Response.<BigDecimal>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(creditAccountEntity.getAdjustAmount())
                    .build();
        }catch (Exception e) {
            log.error("查询用户积分值失败 userId:{}", userId, e);
            return Response.<BigDecimal>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "credit_exchange_sku", method = RequestMethod.POST)
    @Override
    public Response<Boolean> creditExchangeSku(@RequestBody SkuProductShopCartRequestDTO request) {
        try {
            log.info("积分兑换商品开始 userId:{} sku:{}", request.getUserId(), request.getSku());
            // 1. 创建兑换商品sku订单，outBusinessNo 每次创建出一个单号
            UnpaidActivityOrderEntity unpaidActivityOrder = raffleActivityAccountQuotaService.createOrder(SkuRechargeEntity.builder()
                    .userId(request.getUserId())
                    .sku(request.getSku())
                    .outBusinessNo(RandomStringUtils.randomNumeric(12))
                    .orderTradeType(OrderTradeTypeVO.credit_pay_trade)
                    .build());
            log.info("积分兑换商品，创建订单完成 userId:{} sku:{} outBusinessNo:{}", request.getUserId(), request.getSku(), unpaidActivityOrder.getOutBusinessNo());

            // 2.支付兑换商品
            String orderId = creditAdjustService.createOrder(TradeEntity.builder()
                    .userId(unpaidActivityOrder.getUserId())
                    .tradeName(TradeNameVO.CONVERT_SKU)
                    .tradeType(TradeTypeVO.REVERSE)
                    .amount(unpaidActivityOrder.getPayPriceAmount())
                    .outBusinessNo(unpaidActivityOrder.getOutBusinessNo())
                    .build());

            log.info("积分兑换商品，支付订单完成  userId:{} sku:{} orderId:{}", request.getUserId(), request.getSku(), orderId);

            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(true)
                    .build();
        }catch (Exception e){
            log.error("积分兑换商品失败 userId:{} sku:{}", request.getUserId(), request.getSku(), e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }
}







