package com.alphalion.crawl.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author SongBaoYu
 * @date 2018/2/5 下午9:51
 */

@Data
@AllArgsConstructor
public class ArgItem {

    private Class<?> type;
    private Object value;
}
