package com.alphalion.crawl.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alphalion.crawl.mapper.BusinessDateEntityMapper;
import com.alphalion.crawl.mapper.entity.BusinessDateEntity;
import com.alphalion.crawl.service.ICacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SongBaoYu
 * @date 2018/2/7 上午11:08
 */

@Service
public class CacheServiceImpl implements ICacheService {

    @Autowired
    private BusinessDateEntityMapper businessDateEntityMapper;

    Set<String> CACHE_KEYS = new HashSet<>(Arrays.asList(BUSINESS_DATE_CACHE_KEY));
    private Map<String, Object> cache = new ConcurrentHashMap<>();


    @Override
    public boolean validCacheKey(String key) {
        boolean contains = CACHE_KEYS.contains(key);
        return contains;
    }

    @Override
    public Date getBusinessDate() {
        Date date = (Date) cache.get(BUSINESS_DATE_CACHE_KEY);
        if (null == date) {
            BusinessDateEntity businessDateEntity = businessDateEntityMapper.selectOne(null);
            cache.put(BUSINESS_DATE_CACHE_KEY, businessDateEntity.getBusiness_date());
            return businessDateEntity.getBusiness_date();
        }
        return date;
    }

    @Override
    public boolean updateCacheByKey(String key, Object value) {
        boolean validCacheKey = validCacheKey(key);
        if (!validCacheKey) {
            return false;
        }

        cache.put(key, value);
        return true;
    }

    @Override
    public Object queryCacheValue(String key) {
        Object val = cache.get(key);
        return val;
    }
}
