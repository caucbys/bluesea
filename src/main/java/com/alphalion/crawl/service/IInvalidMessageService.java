package com.alphalion.crawl.service;

import java.util.List;

/**
 * @author SongBaoYu
 * @date 2018/1/9 上午10:37
 */
public interface IInvalidMessageService {

    void seekProduct(List<String> invalidValues);

    List<String> processInvalidProducts();

    int updInvalidMsgStaById(long id);

    int updInvalidMsgStaByIds(List<Long> ids);

}
