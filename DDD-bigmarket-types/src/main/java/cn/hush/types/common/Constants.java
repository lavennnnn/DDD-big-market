package cn.hush.types.common;



public class Constants {

    public final static String SPLIT = ",";
    public static final String SPACE = " ";
    public static final String COLON = ":";
    public static final String UNDERLINE = "_";

    public static class RedisKey {
        public static String ACTIVITY_ACCOUNT_LOCK = "activity_account_lock_";
        public static String ACTIVITY_ACCOUNT_UPDATE_LOCK = "activity_account_update_lock_";
        public static String STRATEGY_AWARD_KEY = "big_market_strategy_award_key_";
        public static String STRATEGY_AWARD_LIST_KEY = "big_market_strategy_award_list_key_";
        public static String STRATEGY_RATE_TABLE_KEY = "big_market_strategy_rate_table_key_";
        public static String STRATEGY_RATE_RANGE_KEY = "big_market_strategy_rate_range_key_";
        public static String STRATEGY_KEY = "big_market_strategy_key_";
        public static String RULE_TREE_VO_KEY = "rule_tree_vo_key_";
        public static String STRATEGY_AWARD_COUNT_KEY = "strategy_award_count_key_";
        public static String STRATEGY_AWARD_COUNT_QUEUE_KEY = "strategy_award_count_queue_key_";
        public static String ACTIVITY_COUNT_KEY = "activity_count_key_";
        public static String ACTIVITY_SKU_KEY = "activity_sku_key_";
        public static String ACTIVITY_KEY = "activity_key_";
        public static String ACTIVITY_SKU_STOCK_COUNT_KEY = "activity_sku_stock_count_key_";
        public static String ACTIVITY_SKU_COUNT_QUERY_KEY = "activity_sku_count_query_key_";
        public static String STRATEGY_RULE_WEIGHT_KEY = "strategy_rule_weight_key_";
        public static String USER_CREDIT_ACCOUNT_LOCK = "user_credit_account_lock_";
        public static String STRATEGY_AWARD_COUNT_QUERY_KEY = "strategy_award_count_query_key_";
        public static String STRATEGY_ARMORY_ALGORITHM_KEY = "strategy_armory_algorithm_key_";

    }

    /**
     * 状态常量，启用或者禁用
     */
    public static class StatusConstant {
        //启用
        public static final Integer ENABLE = 1;

        //禁用
        public static final Integer DISABLE = 0;
    }

    /**
     * jwt常量
     */
    public static class JwtClaimsConstant {
        public static final String EMP_ID = "empId";
        public static final String USER_ID = "userId";
        public static final String USERNAME = "username";
        public static final String NAME = "name";
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
