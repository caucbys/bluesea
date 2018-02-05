package com.alphalion.crawl.mapper;

import com.alphalion.crawl.application.base.BaseMapper;
import com.alphalion.crawl.mapper.entity.ProductSymbolsNetEntity;

import java.util.List;


/**
 * 功能描述:ProductSymbolsNetEntity的mapper映射类
 *
 * @author SongBaoYu
 * @date 2018年01月10日 20:41:25
 */
public interface ProductSymbolsNetEntityMapper extends BaseMapper<ProductSymbolsNetEntity> {
    // 成员方法;

    int batchInsertSymbolsIgnoreErrors(List<ProductSymbolsNetEntity> productSymbolsNetEntities);


}
