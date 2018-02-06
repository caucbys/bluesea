package com.alphalion.crawl.application.constant;

/**
 * @author SongBaoYu
 * @date 2018/1/10 下午6:04
 */
public class InvalidMessageConstant {


    public static final class Values {
        public static String ADD = "ADD";
        public static String CXL = "CXL";
    }


    public static final class Status {
        public static final String INVALID_MESSAGE_ERROR = "E";//待修改
        public static final String INVALID_MESSAGE_PUBLISH = "P";//修改完成
        public static final String INVALID_MESSAGE_DELETED = "D";//已处理 or no longer need to be reprocessed.
    }

    public static final class Reason{
        //invalid Message reason
        public static final String INVALID_REASON_ACCOUNT_ID_NOT_EXIST = "account_id_not_exist";
        public static final String INVALID_REASON_PRODUCT_ID_NOT_EXIST = "product_id_not_exist";
        public static final String INVALID_REASON_BLOTTER_CODE_NOT_EXIST = "blotter_code_not_exist";
        public static final String INVALID_REASON_CURRENCY_CODE_NOT_EXIST = "currency_code_not_exist";
        public static final String INVALID_REASON_EXCHNAGE_NOT_EXIST = "exchange_not_exist";
        public static final String INVALID_REASON_CONTRA_BROKER_NOT_EXIST = "contra_broker_not_exist";
        public static final String INVALID_REASON_EXECUTION_BROKER_NOT_EXIST = "execution_broker_not_exist";
        public static final String INVALID_REASON_SECURITY_PRICE_NOT_EXIST = "security_price_not_exist";
        public static final String INVALID_REASON_CURRENCY_PRICE_NOT_EXIST = "currency_price_not_exist";
        public static final String INVALID_REASON_MARGIN_RATE_NOT_EXIST = "margin_rate_not_exist";
        public static final String INVALID_REASON_LOANET_TRANS_DESC_CODE = "unknown_loanet_trans_desc_code";
        public static final String INVALID_REASON_MISSING_ORIGINAL_LOANET_TRANSACTION = "missing_original_loanet_transaction";

        public static final String INVALID_REASON_JOURNAL_ID_NOT_EXIST = "journal_id_not_exist";
        public static final String INVALID_REASON_INSERT_JOURNAL_ERR = "insert_journal_sql_error";/**/
    }
}
