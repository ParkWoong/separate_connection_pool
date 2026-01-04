package com.example.connection_pool.mapper.main;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.connection_pool.dto.DataRow;

@Mapper
public interface DataMapper {
    DataRow selectById(@Param("id") long id);
}
