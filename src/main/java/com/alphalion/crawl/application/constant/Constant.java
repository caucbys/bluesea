/*
 * Copyright (C) Bluesea Fintech, Inc, 北京晨灏科技有限公司，Bluesea Fintech USA, LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 */

package com.alphalion.crawl.application.constant;

import com.google.common.collect.Sets;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

/**
 * Created by liwt on 7/18/16.
 */
public class Constant {

    public static final String SEP = "#";

    public enum StockMarket {
        US, HK, HKNTL
    }

    public static final String MARKET_US = "US";
    public static final String MARKET_HK = "HK";
    public static final String MARKET_CN = "CN";
    public static final String MARKET_HKNTL = "HKNTL";

    public static final String SELL = "SELL";
    public static final String BUY = "BUY";

    public static final String CURRENCY_US = "USD";
    public static final String CURRENCY_HK = "HKD";
    public static final String CURRENCY_CN = "CNH";

    public static final String SEC_OPT = "OPT";
    public static final String SEC_STK = "STK";
    public static final String SEC_FUT = "FUT";


    public static final String DEFAULT_SUB_ACCOUNT = "S00";

    public static final String ACCOUNT_SUB_TYPE_DVP = "DVP";

    public static final Set<String> ACCOUNT_SUB_TYPE_SET = Sets.newHashSet("CASH", "CSTS", "CSTH", "DVP", "PB", "MRT", "MDT", "MPM", "PAIB");


    //account_class
    public static final String ACCOUNT_TYPE_CUSTOMER = "CU";
    public static final String ACCOUNT_TYPE_CONTRA_BROKER = "CB";
    public static final String ACCOUNT_TYPE_PROP_TRADE = "PT";
    public static final String ACCOUNT_TYPE_STREET_SIDE = "ST";
    public static final String ACCOUNT_TYPE_GENERAL_LEGER = "GL";

    public static final Set<String> ACCOUNT_CLASS_SET = Sets.newHashSet(ACCOUNT_TYPE_CUSTOMER, ACCOUNT_TYPE_CONTRA_BROKER, ACCOUNT_TYPE_PROP_TRADE, ACCOUNT_TYPE_STREET_SIDE, ACCOUNT_TYPE_GENERAL_LEGER);

    //subtype
    public static final String SUBTYPE_CASH_COD = "01";
    public static final String SUBTYPE_CASH_HIC = "02";
    public static final String SUBTYPE_MARGIN = "06";
    public static final String SUBTYPE_SHORT = "05";
    public static final String SUBTYPE_SUPSENSE = "09";
    public static final String SUBTYPE_PROP = "07";
    public static final String SUBTYPE_IRA = "03";
    public static final String SUBTYPE_FULLY_PAID_LENDING = "12";
    public static final String SUBTYPE_GL_ASSET = "15";
    public static final String SUBTYPE_GL_LIABILITY = "16";
    public static final String SUBTYPE_GL_CAPITAL = "17";
    public static final String SUBTYPE_GL_REVENUE = "18";
    public static final String SUBTYPE_GL_EXPENSE = "19";


    //entity_type
    public static final String ENTITY_INDIVIDUAL = "Individual";
    public static final String ENTITY_CORPORATION = "Corporation";
    public static final String ENTITY_LLC = "LLC";
    public static final String ENTITY_PARTNERSHIP = "Partnership";
    public static final String ENTITY_BUSINESS_TRUST = "Business Trust";
    public static final String ENTITY_TRUST = "Trust";
    public static final String ENTITY_LIMITED_PARTNERSHIP = "Limited Partnership";
    public static final String ENTITY_GOVERNMENT_AGENCY = "Government Agency";

    public static final Set<String> ENTITY_SET = Sets.newHashSet(ENTITY_INDIVIDUAL, ENTITY_CORPORATION, ENTITY_LLC, ENTITY_PARTNERSHIP,
            ENTITY_BUSINESS_TRUST, ENTITY_TRUST, ENTITY_LIMITED_PARTNERSHIP, ENTITY_GOVERNMENT_AGENCY);

    public static final String TAX_LOT_AVG = "AVGCOST";
    public static final String TAX_LOT_FIFO = "FIFO";
    public static final String TAX_LOT_LIFO = "LIFO";
    public static final String TAX_LOT_DOLLAR = "DLIFO";
    public static final String TAX_LOT_SPECIFIC = "SPECLOT";
    public static final String TAX_LOT_LOWEST = "LOCOST";
    public static final String TAX_LOT_HIGEST = "HICOST";
    public static final String TAX_LOT_HARVEST = "TXHRVST";

    public static final Set<String> TAX_LOT_SET = Sets.newHashSet(TAX_LOT_AVG, TAX_LOT_FIFO, TAX_LOT_LIFO, TAX_LOT_DOLLAR,
            TAX_LOT_SPECIFIC, TAX_LOT_LOWEST, TAX_LOT_HIGEST, TAX_LOT_HARVEST);

    //journal
    public static final String JOURNAL_ADJUSTMENT = "ADJ";
    public static final String JOURNAL_MEMO_ADJUSTMENT = "MEMO";
    public static final String JOURNAL_EXPIRE = "EXP";
    public static final String JOURNAL_SETTLEMENT = "SETT";
    public static final String JOURNAL_EXERCISE = "EXER";
    public static final String JOURNAL_ASSIGNMENT = "ASSN";
    public static final String JOURNAL_CORPORATE_ACTION = "CORPA";

    //posting rules
    public static final String ACCOUNT_DEBIT = "D";
    public static final String ACCOUNT_CREDIT = "C";
    public static final String POSTING_RULE_SECURITY = "SECURITY";
    public static final String POSTING_RULE_CASH = "CASH";
    public static final String POSTING_RULE_SIDE_ASSET = "A";
    public static final String POSTING_RULE_SIDE_LIABLILITY = "L";
    public static final String POSTING_RULE_INDICATOE_BUY = "B";
    public static final String POSTING_RULE_INDICATOE_SELL = "S";
    public static final String POSTING_RULE_INDICATOE_PAY = "P";
    public static final String POSTING_RULE_INDICATOE_RECEIVE = "R";


    //product type: security or cash
    public static final String PRODUCT_TYPE_SECURITY = "S";
    public static final String PRODUCT_TYPE_CASH = "C";

    //交易类型
    public static final String TRANSACTION_TRADE = "TRD";
    public static final String TRANSACTION_JOURNAL = "JNL";
    public static final String TRANSACTION_CF = "CF";

    //定义一个开始时间:2000-00-00 00:00:00    让每个时区都是2000-01-01
    public static final Timestamp START_TIMESTAMP = Timestamp.valueOf(LocalDateTime.of(2000, 01, 01, 0, 0, 0));

    public static final Date START_DATE = new Date(START_TIMESTAMP.getTime());

    public static final java.sql.Date START_SQL_DATE = new java.sql.Date(START_TIMESTAMP.getTime());

    //定义一个无穷时间:4821-12-27 00:00:00    让每个时区都是4821-12-27
    public static final Timestamp INFINITY_TIMESTAMP = Timestamp.valueOf(LocalDateTime.of(4821, 12, 27, 0, 0, 0));

    public static final Date INFINITY_DATE = new Date(INFINITY_TIMESTAMP.getTime());//new Date(90l * 1000 * 1000 * 1000 * 1000);

    public static final java.sql.Date INFINITY_SQL_DATE = new java.sql.Date(INFINITY_DATE.getTime());


    /**
     * yyyy-MM-dd HH:mm:ss格式
     */
    public static final String INFINITY_TIME_STR = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(INFINITY_DATE);

    /**
     * yyyy-MM-dd格式
     */
    public static final String INFINITY_DATE_STR = new SimpleDateFormat("yyyy-MM-dd").format(INFINITY_DATE);

    //module-name
    public static final String MODULE_EARTH = "earth";
    public static final String MODULE_APOLLO = "apollo";
    public static final String MODULE_HALLEY = "halley";
    public static final String MODULE_REFERENCE = "reference";
    public static final String MODULE_MS = "ms";//管理系统
    public static final String MODULE_MARGIN = "margin";
    public static final String MODULE_SEG = "seg";
    public static final String MODULE_EOD_CONTROL = "eod_control";


    //syn_table 全局同步变量
    public static final String SYN_NAME = "syn_name";
    public static final String SYN_TYPE = "general";

}
