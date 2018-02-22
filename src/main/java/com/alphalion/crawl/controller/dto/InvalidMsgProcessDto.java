package com.alphalion.crawl.controller.dto;

import lombok.Data;

import java.util.List;

/**
 * @author SongBaoYu
 * @date 2018/2/22 上午10:51
 */

@Data
public class InvalidMsgProcessDto {
    List<String> succ;
    List<String> fail;
}
