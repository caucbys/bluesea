package com.alphalion.crawl.controller;

import com.alphalion.crawl.controller.dto.CacheDto;
import com.alphalion.crawl.controller.dto.Result;
import com.alphalion.crawl.service.ICacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author SongBaoYu
 * @date 2018/2/7 上午11:24
 */
@Api(description = "缓存类相关接口")
@RestController
@RequestMapping("/cache")
public class CacheController {

    @Autowired
    private ICacheService cacheService;

    @ApiOperation(httpMethod = "POST", value = "查询缓存数据", notes = "查询缓存数据")
    @RequestMapping(method = RequestMethod.POST, value = "/queryCacheValue/{key}")
    @ResponseBody
    public Result queryCacheValueByKey(@PathVariable String key) {
        Object val = cacheService.queryCacheValue(key);
        return Result.successed(val);
    }

    @ApiOperation(httpMethod = "POST", value = "查询缓存数据", notes = "查询缓存数据")
    @RequestMapping(method = RequestMethod.POST, value = "/resetCacheValue")
    @ResponseBody
    public Result resetCacheValue(@RequestBody CacheDto cacheDto) {
        boolean res = cacheService.updateCacheByKey(cacheDto.getKey(), cacheDto.getValue());
        return new Result("操作成功", res);
    }


}
