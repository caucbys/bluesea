package com.alphalion.crawl.controller.dto;

import lombok.Data;

/**
 * @author SongBaoYu
 * @date 2018/1/16 上午10:53
 */

@Data
public class AuthRequest {

    private String userName;
    private String pwd;
    private String token;
}
