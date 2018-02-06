/* Copyright (C) Bluesea Fintech, Inc, 北京晨灏科技有限公司，Bluesea Fintech USA, LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.alphalion.crawl.application.constant;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Create by Zhang La on 2017/3/31
 */
public class ProductConstant {

    public static final String PRODUCT_ID_SEQ = "PRODUCT_ID_SEQ";

    /**
     * 产品交易状态
     */
    public static final int STATUS_ACTIVE = 1, STATUS_INACTIVE = 0;


    public static final class Asset{
        public static final String EQUITY = "Equity";
        public static final String OPTION = "Option";
        public static final String FUTURE = "Future";
        public static final String FIXED_INCOME = "Fixed Income";
        public static final String MUTUAL_FUNDS = "Mutual Funds";

        public static final Set<String> SET = Sets.newHashSet(EQUITY, OPTION, FUTURE, FIXED_INCOME, MUTUAL_FUNDS);

        public static final String STRINGS = EQUITY + "," + OPTION + "," + FUTURE + "," + FIXED_INCOME + "," + MUTUAL_FUNDS;
    }

    public static final class Security{
        //Equity
        public static final String ADR = "ADR";
        public static final String COMMON_STOCK = "Common Stock";
        public static final String PREFERRED_STOCK = "Preferred Stock";
        public static final String RIGHTS = "Rights";
        public static final String WARRANTS = "Warrants";

        //Fixed Income
        public static final String COMMERCIAL_PAPER = "Commercial Paper";
        public static final String CERTIFICATE_OF_DEPOSIT = "Certificate Of Deposit";

        //Option
        public static final String INDEX = "Index";
        public static final String STOCK_OPTIONS = "Stock Options";

        //Mutual Funds
        public static final String MONEY_MARKET_FUND = "Money Market Fund";

        public static final Multimap<String, String> MULTIMAP = HashMultimap.create();

        static {
            MULTIMAP.put(Asset.EQUITY, ADR);
            MULTIMAP.put(Asset.EQUITY, COMMON_STOCK);
            MULTIMAP.put(Asset.EQUITY, PREFERRED_STOCK);
            MULTIMAP.put(Asset.EQUITY, RIGHTS);
            MULTIMAP.put(Asset.EQUITY, WARRANTS);

            MULTIMAP.put(Asset.FIXED_INCOME, COMMERCIAL_PAPER);
            MULTIMAP.put(Asset.FIXED_INCOME, CERTIFICATE_OF_DEPOSIT);

            MULTIMAP.put(Asset.OPTION, INDEX);
            MULTIMAP.put(Asset.OPTION, STOCK_OPTIONS);

            MULTIMAP.put(Asset.MUTUAL_FUNDS, MONEY_MARKET_FUND);
        }

        public static final String STRINGS = ADR + "," + COMMON_STOCK + "," + PREFERRED_STOCK + "," + RIGHTS + "," + WARRANTS + "," + COMMERCIAL_PAPER
                + "," + CERTIFICATE_OF_DEPOSIT + "," + INDEX + "," + STOCK_OPTIONS + "," + MONEY_MARKET_FUND;
    }

    public static final class SecuritySub{
        //Equity
        public static final String ETF = "ETF";

        //Fixed Income
        public static final String CONVERTIBLE = "Convertible";
        public static final String GNMA = "GNMA";
        public static final String FNMA = "FNMA";
        public static final String GENERAL_OBLIGATION = "General Obligation";
        public static final String REGISTERED = "Registered";
        public static final String REVENUE = "Revenue";
        public static final String FLOATING_RATE = "Floating Rate";
        public static final String VARIABLE = "Variable";
        public static final String COUPON = "Coupon";

        //Option
        public static final String ON_INTEREST_RATES = "On Interest Rates";
        public static final String ON_CURRENCIES = "On Currencies";
        public static final String ON_STOCK_INDEXES = "On Stock Indexes";
        public static final String ON_COMMODITY = "On Commodity";

        //security : security sub
        public static final Multimap<String, String> MULTIMAP = HashMultimap.create();

        static {
//            MULTIMAP.put(Asset.EQUITY, ETF);
//
//            MULTIMAP.put(Asset.FIXED_INCOME, CONVERTIBLE);
//            MULTIMAP.put(Asset.FIXED_INCOME, GNMA);
//            MULTIMAP.put(Asset.FIXED_INCOME, FNMA);
//            MULTIMAP.put(Asset.FIXED_INCOME, GENERAL_OBLIGATION);
//            MULTIMAP.put(Asset.FIXED_INCOME, REGISTERED);
//            MULTIMAP.put(Asset.FIXED_INCOME, REVENUE);
//            MULTIMAP.put(Asset.FIXED_INCOME, FLOATING_RATE);
//            MULTIMAP.put(Asset.FIXED_INCOME, VARIABLE);
//            MULTIMAP.put(Asset.FIXED_INCOME, COUPON);
//
//            MULTIMAP.put(Asset.OPTION, ON_INTEREST_RATES);
//            MULTIMAP.put(Asset.OPTION, ON_CURRENCIES);
//            MULTIMAP.put(Asset.OPTION, ON_STOCK_INDEXES);
//            MULTIMAP.put(Asset.OPTION, ON_COMMODITY);
        }

        public static final String STRINGS = ETF + "," + CONVERTIBLE + "," + GNMA + "," + FNMA + "," + GENERAL_OBLIGATION + "," + REGISTERED + ","
                + REVENUE + "," + FLOATING_RATE + "," + VARIABLE + "," + COUPON + "," + ON_INTEREST_RATES + "," + ON_CURRENCIES + "," + ON_STOCK_INDEXES + "," + ON_COMMODITY;
    }

    public static final class IssueCategories{
        public static final String NON_US_CANANDIAN = "Non Us Canandian";
        public static final String US_CORP = "US Corp";
        public static final String MUNICIPAL = "Municipal";
        public static final String DUTCH_DOMESTIC = "Dutch Domestic";
        public static final String BULLDOG = "Bulldog";
        public static final String EUROBOND = "Eurobond";
        public static final String FOREIGN_ISSUE = "Foreign Issue";
        public static final String US_GOVT = "US Govt";
        public static final String SHOGUN = "Shogun";
        public static final String SAMURAI = "Samurai";
        public static final String MARKET_Index = "Market Index";
        public static final String YANKEE = "Yankee";

        public static final Set<String> SET = Sets.newHashSet(NON_US_CANANDIAN, US_CORP, MUNICIPAL, DUTCH_DOMESTIC, BULLDOG,
                EUROBOND, FOREIGN_ISSUE, US_GOVT, SHOGUN, SAMURAI, MARKET_Index, YANKEE);

        public static final String STRINGS = NON_US_CANANDIAN + "," + US_CORP + "," + MUNICIPAL + "," + DUTCH_DOMESTIC + "," + BULLDOG + "," +
                EUROBOND + "," + FOREIGN_ISSUE + "," + US_GOVT + "," + SHOGUN + "," + SAMURAI + "," + MARKET_Index + "," + YANKEE;
    }

    public static final class TradingStatus{
        public static final String TRADING = "T";

        public static final Set<String> SETS = Sets.newHashSet(TRADING);
    }

    public static final class SymbolTypes{
        public static final String RIC = "RIC";

        public static final String ALTERNATE_SYMBOL = "ATSB";

        public static final String BLOOMBERG = "BMBG";

        public static final String CUSIP = "CUSIP";

        public static final String SEDOL = "SEDOL";

        public static final String ADP = "ADP";

        public static final String ISIN = "ISIN";

        public static final String CINS = "CINS";

        public static final String IDC = "IDC";

        public static final String SYMBOL = "SYMBOL";

        public static final String WKN = "WKN";

        public static final Set<String> SET = Sets.newHashSet(RIC, ALTERNATE_SYMBOL, BLOOMBERG, CUSIP, SEDOL, ADP, ISIN, CINS, IDC, SYMBOL,WKN);

        public static final Set<String> REQUIRED = Sets.newHashSet(CUSIP, ISIN);
    }


    public static class PutCall{
        public static final String PUT = "P", CALL = "C";

        public static final Set<String> SETS = Sets.newHashSet(PUT, CALL);
    }
}
