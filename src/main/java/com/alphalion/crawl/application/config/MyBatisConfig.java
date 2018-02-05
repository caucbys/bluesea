package com.alphalion.crawl.application.config;

import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.logging.log4j.Log4jImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 功能描述:MyBatis初始化配置;
 *
 * @author SongBaoyu
 * @Date:2017年7月30日 下午1:59:11
 */
@Configuration
@EnableTransactionManagement
public class MyBatisConfig implements TransactionManagementConfigurer {
    @Autowired
    private DataSource dataSource;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();

        // 基本信息;
        bean.setDataSource(dataSource);
        List<String> packages = new ArrayList<String>(1);
        packages.add("com.alphalion.crawl.mapper.entity");
        bean.setTypeAliasesPackage(StringUtils.join(packages, ","));

        // 分页插件;
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();

        properties.setProperty("reasonable", "true");//如果pageNum<1会查询第一页，如果pageNum>pages会查询最后一页
        properties.setProperty("offsetAsPageNum", "true");//将RowBounds第一个参数offset当成pageNum页码使用
        properties.setProperty("rowBoundsWithCount", "true");//使用RowBounds分页会进行count查询
        properties.setProperty("supportMethodsArguments", "true");//支持通过Mapper接口参数来传递分页参数
        properties.setProperty("returnPageInfo", "check");//always总是返回PageInfo类型,check检查返回类型是否为PageInfo,none返回Page
        properties.setProperty("pageSizeZero", "true");//如果pageSize=0或者RowBounds.limit = 0就会查询出全部的结果
        properties.setProperty("params", "count=countSql");//用于从Map或ServletRequest中取值
        pageHelper.setProperties(properties);

        // 分页拦截器;
        bean.setPlugins(new Interceptor[]{pageHelper});


        // 自动加载XML格式的SQL映射文件;
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath*:mapper/*.xml"));
            bean.getObject().getConfiguration().setLogImpl(Log4jImpl.class);
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


    @Override
    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

}
