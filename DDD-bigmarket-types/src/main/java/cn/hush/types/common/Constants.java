package cn.hush.types.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Constants {

    public final static String SPLIT = ",";

    public static class RedisKey {
        public static String STRATEGY_AWARD_KEY = "big_market_strategy_award_key_";
        public static String STRATEGY_RATE_TABLE_KEY = "big_market_strategy_rate_table_key_";
        public static String STRATEGY_RATE_RANGE_KEY = "big_market_strategy_rate_range_key_";
    }

//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Getter
//    public enum ResponseCode {
//
//        SUCCESS("0000", "成功"),
//        UN_ERROR("0001", "未知失败"),
//        ILLEGAL_PARAMETER("0002", "非法参数"),
//        ;
//
//        private String code;
//        private String info;
//
//    }

}
