package com.example.connection_pool.service;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.connection_pool.dto.DataRow;
import com.example.connection_pool.mapper.log.LogMapper;
import com.example.connection_pool.mapper.main.DataMapper;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestService {
    private static final Logger log = LoggerFactory.getLogger(TestService.class);

    private final DataMapper dataMapper;
    private final LogMapper appLogMapper;

    
    @Resource(name = "mainDataSource")
    private final DataSource mainDataSource;

    @Resource(name = "logDataSource")
    private final DataSource logDataSource;

    public DataRow fetchData(long id) {
        log.info("[POOL] data SELECT will use: {}", poolName(mainDataSource));
        return dataMapper.selectById(id);
    }

    public int writeLog(String message) {
        log.info("[POOL] log INSERT will use: {}", poolName(logDataSource));
        return appLogMapper.insertLog(message);
    }

    // 한 번의 요청에서 둘 다 실행 (메인 SELECT + 로그 INSERT)
    public DataRow fetchAndLog(long id) {
        DataRow row = fetchData(id);
        String msg = (row == null)
                ? "fetchAndLog: id=" + id + " not found"
                : "fetchAndLog: id=" + id + ", name=" + row.getName();
        try {
            writeLog(msg);
        } catch (Exception e) {
            log.warn("log insert failed reason={}", e.toString());
        }
        return row;
    }

    private String poolName(DataSource ds) {
        if (ds instanceof HikariDataSource hikari) 
            return hikari.getPoolName();
        return ds.getClass().getSimpleName();
    }
}
