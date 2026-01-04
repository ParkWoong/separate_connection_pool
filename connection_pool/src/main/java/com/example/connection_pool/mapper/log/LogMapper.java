package com.example.connection_pool.mapper.log;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LogMapper {
    int insertLog(@Param("message") String message);
}
