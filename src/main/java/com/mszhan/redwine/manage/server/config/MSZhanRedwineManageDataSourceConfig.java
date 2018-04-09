package com.mszhan.redwine.manage.server.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.mszhan.redwine.manage.server.core.Mapper;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Description: 数据源配置
 * @Author: iblilife@163.com
 * @Date: 22:24 2017/12/24
 */
@Configuration
@MapperScan(basePackages = MSZhanRedwineManageDataSourceConfig.SCAN_PACKAGE, sqlSessionFactoryRef = MSZhanRedwineManageDataSourceConfig.BEAN_SQL_SESSION_FACTORY_NAME)
public class MSZhanRedwineManageDataSourceConfig {

    /* 多数据原配置 - 修改开始 */
    static final String SCAN_PACKAGE = "com.mszhan.redwine.manage.server.dao.mszhanRedwineManage";
    static final String MAPPER_LOCATION = "classpath*:mapper/mszhanRedwineManage/*.xml";
    static final String DATA_SOURCE_PROPERTIES_PREFIX = "spring.datasource.mszhanRedwineManage";
    static final String BEAN_NAME_PREFIX = "mszhanRedwineManage";
    /* 多数据原配置 - 修改结束 */

    public static final String BEAN_SQL_SESSION_FACTORY_NAME = BEAN_NAME_PREFIX + "SqlSessionFactory";
    public static final String BEAN_DATA_SOURCE_NAME = BEAN_NAME_PREFIX + "DataSource";
    public static final String BEAN_TRANSACTION_MANAGER_NAME = BEAN_NAME_PREFIX + "TransactionManager";
    public static final String BEAN_TEST_SQL_SESSION_TEMPLATE_NAME = BEAN_NAME_PREFIX + "TestSqlSessionTemplate";
    public static final String BEAN_MAPPER_HELPER_NAME = BEAN_NAME_PREFIX + "MapperHelper";

    @Primary
    @ConfigurationProperties(prefix = DATA_SOURCE_PROPERTIES_PREFIX)
    @Bean(name = BEAN_DATA_SOURCE_NAME)
    public DruidDataSource dataSource() {
        return new DruidDataSource();
    }

    @Primary
    @Bean(name = BEAN_TRANSACTION_MANAGER_NAME)
    public DataSourceTransactionManager transactionManager(
            @Qualifier(value = BEAN_DATA_SOURCE_NAME) DataSource dataSource
    ) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = BEAN_SQL_SESSION_FACTORY_NAME)
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier(value = BEAN_DATA_SOURCE_NAME) DataSource dataSource
    ) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));

        //分页插件
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties pageProp = new Properties();
        pageProp.setProperty("helperDialect", "mysql");   //方言
        pageInterceptor.setProperties(pageProp);

        //添加插件
        sessionFactory.setPlugins(new Interceptor[]{pageInterceptor});

        return sessionFactory.getObject();
    }

    @Primary
    @Bean(name = BEAN_TEST_SQL_SESSION_TEMPLATE_NAME)
    public SqlSessionTemplate testSqlSessionTemplate(
            @Qualifier(value = BEAN_SQL_SESSION_FACTORY_NAME) SqlSessionFactory sqlSessionFactory
    ) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * Mybatis 通用Mapper配置
     */
    @Bean(name = BEAN_MAPPER_HELPER_NAME)
    public MapperHelper mapperHelper(
            @Qualifier(value = BEAN_SQL_SESSION_FACTORY_NAME) SqlSessionFactory sqlSessionFactory
    ) {
        MapperHelper mapperHelper = new MapperHelper();
        //特殊配置
        Config config = new Config();
        config.setNotEmpty(false);
        config.setIDENTITY("MYSQL");
        //更多详细配置: http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/2.Integration.md
        mapperHelper.setConfig(config);
        mapperHelper.registerMapper(Mapper.class);
        mapperHelper.processConfiguration(sqlSessionFactory.getConfiguration());

        return mapperHelper;
    }
}
