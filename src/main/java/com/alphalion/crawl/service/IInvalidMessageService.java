package com.alphalion.crawl.service;

import java.util.List;
import java.util.Set;

/**
 * @author SongBaoYu
 * @date 2018/1/9 上午10:37
 */
public interface IInvalidMessageService {

    void seekProduct(List<String> invalidValues);

    Set<String> processInvalidProducts();

    int updInvalidMsgStaById(long id);

}
