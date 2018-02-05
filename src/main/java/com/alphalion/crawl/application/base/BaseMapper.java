package com.alphalion.crawl.application.base;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.common.ids.DeleteByIdsMapper;

/**
 * 功能描述：
 *
 * @Author SongBaoYu
 * @Date:2017/9/12 14:42
 */
public interface BaseMapper<T> extends Mapper<T>,MySqlMapper<T>,DeleteByIdsMapper<T> {
}
