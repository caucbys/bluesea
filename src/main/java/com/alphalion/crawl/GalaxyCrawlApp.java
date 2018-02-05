package com.alphalion.crawl;

import com.alphalion.crawl.application.util.SpringBeanTools;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author SongBaoYu
 * @date 2018/1/9 下午4:48
 */

@SpringBootApplication
public class GalaxyCrawlApp {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(GalaxyCrawlApp.class);
        SpringBeanTools.setApplicationContext1(applicationContext);
    }
}
