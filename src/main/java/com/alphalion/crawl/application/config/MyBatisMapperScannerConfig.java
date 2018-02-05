package com.alphalion.crawl.application.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 功能描述:MyBatis自动加载的映射器接口;
 * @Date:2017年7月28日 下午2:12:40
 * @author SongBaoYu
 */
@Configuration
@AutoConfigureAfter(MyBatisConfig.class)
public class MyBatisMapperScannerConfig 
{
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer()
    {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        // Mapper映射器接口所在位置;
        List<String> packages=new ArrayList<>(1);
        packages.add("com.alphalion.crawl.mapper");
        mapperScannerConfigurer.setBasePackage(StringUtils.join(packages,","));
        // 映射器基类所在位置;
        Properties properties = new Properties();
        properties.setProperty("mappers", "com.alphalion.crawl.application.base.BaseMapper");
        properties.setProperty("notEmpty", "false");
        properties.setProperty("IDENTITY", "MYSQL");
        mapperScannerConfigurer.setProperties(properties);
        
        return mapperScannerConfigurer;
    }

}
