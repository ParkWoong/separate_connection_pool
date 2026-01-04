package com.example.connection_pool.config;

import javax.sql.DataSource;

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

import com.zaxxer.hikari.HikariDataSource;
@Configuration
public class BatisConfig {
    
    @Bean(name = "mainDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.main")
    public DataSource mainDataSource() {
        return new HikariDataSource();
    }

    @Bean(name = "logDataSource")
    @ConfigurationProperties("spring.datasource.log")
    public DataSource logDataSource() {
        return new HikariDataSource();
    }

    @Bean(name = "mainSqlSessionFactory")
    public SqlSessionFactory mainSqlSessionFactory(@Qualifier("mainDataSource") DataSource ds) throws Exception {
        return build(ds, "classpath:/mybatis/main/**/*.xml");
    }

    @Bean(name = "logSqlSessionFactory")
    public SqlSessionFactory logSqlSessionFactory(@Qualifier("logDataSource") DataSource ds) throws Exception {
        return build(ds, "classpath:/mybatis/log/**/*.xml");
    }

    @Bean(name = "mainSqlSessionTemplate")
    public SqlSessionTemplate mainSqlSessionTemplate(@Qualifier("mainSqlSessionFactory") SqlSessionFactory sf) {
        return new SqlSessionTemplate(sf);
    }

    @Bean(name = "logSqlSessionTemplate")
    public SqlSessionTemplate logSqlSessionTemplate(@Qualifier("logSqlSessionFactory") SqlSessionFactory sf) {
        return new SqlSessionTemplate(sf);
    }


    //mapper 패키지로 풀을 강제
    @Configuration
    @MapperScan(basePackages = "com.example.connection_pool.mapper.main", 
                sqlSessionTemplateRef = "mainSqlSessionTemplate")
    static class MainScan {}

    @Configuration
    @MapperScan(basePackages = "com.example.connection_pool.mapper.log", 
                sqlSessionTemplateRef = "logSqlSessionTemplate")
    static class LogScan {}

    private SqlSessionFactory build(DataSource ds, String mapperPath) throws Exception {
        SqlSessionFactoryBean f = new SqlSessionFactoryBean();
        f.setDataSource(ds);
        f.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperPath));
        return f.getObject();
    }

}
