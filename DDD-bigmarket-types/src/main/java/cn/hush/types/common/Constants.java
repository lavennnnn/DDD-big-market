package cn.hush.types.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Constants {

    public final static String SPLIT = ",";
    public static final String SPACE = " ";
    public static final String COLON = ":";
    public static final String UNDERLINE = "_";

    public static class RedisKey {
        public static String STRATEGY_AWARD_KEY = "big_market_strategy_award_key_";
        public static String STRATEGY_RATE_TABLE_KEY = "big_market_strategy_rate_table_key_";
        public static String STRATEGY_RATE_RANGE_KEY = "big_market_strategy_rate_range_key_";
        public static String STRATEGY_KEY = "big_market_strategy_key_";
        public static String RULE_TREE_VO_KEY = "rule_tree_vo_key_";
        public static String STRATEGY_AWARD_COUNT_KEY = "strategy_award_count_key_";
        public static String STRATEGY_AWARD_COUNT_QUEUE_KEY = "strategy_award_count_queue_key_";
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
