package com.example.connection_pool;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {MybatisAutoConfiguration.class})
public class ConnectionPoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConnectionPoolApplication.class, args);
	}

}
