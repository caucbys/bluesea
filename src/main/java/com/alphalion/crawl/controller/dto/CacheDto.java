package com.alphalion.crawl.controller.dto;

import com.alphalion.crawl.service.ICacheService;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author SongBaoYu
 * @date 2018/2/7 上午11:33
 */
@Data
public class CacheDto {
    @ApiModelProperty(example = ICacheService.BUSINESS_DATE_CACHE_KEY)
    private String key;
    @ApiModelProperty(example = "2018-01-17 00:00")
    private Object value;
}
