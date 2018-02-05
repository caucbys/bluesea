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
}
