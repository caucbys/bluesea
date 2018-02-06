package com.alphalion.crawl.controller.dto;

import lombok.Data;

/**
 * @author SongBaoYu
 * @date 2018/2/5 下午9:08
 */

@Data
public class ClassDto {

    private Class<?> clazz;
    private String method;
    private ArgItem[] args;


}
