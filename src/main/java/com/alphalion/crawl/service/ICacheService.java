package com.alphalion.crawl.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * @author SongBaoYu
 * @date 2018/2/7 上午11:06
 */
public interface ICacheService {

    String BUSINESS_DATE_CACHE_KEY = "business_date_cache_key";

    boolean validCacheKey(String key);

    Date getBusinessDate();

    boolean updateCacheByKey(String key, Object value);

    Object queryCacheValue(String key);
}
