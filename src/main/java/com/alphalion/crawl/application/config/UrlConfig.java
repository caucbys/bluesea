package com.alphalion.crawl.application.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author SongBaoYu
 * @date 2018/1/12 下午2:05
 */

@ConfigurationProperties(prefix = "url")
@Configuration
@Data
public class UrlConfig {

    private String invalidMessageReplay;
    private String userName;
    private String password;
    private String login;


}
