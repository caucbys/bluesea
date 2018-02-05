package com.alphalion.crawl.mapper;

import com.alphalion.crawl.application.base.BaseMapper;
import com.alphalion.crawl.mapper.entity.ProductEntity;


/**
 * 功能描述:ProductEntity的mapper映射类
 * 
 * @author SongBaoYu
 * @date 2018年01月10日 13:36:16
 */
public interface ProductEntityMapper extends BaseMapper<ProductEntity>
{
	// 成员方法;
    Long selectMaxProductId();
		
}
