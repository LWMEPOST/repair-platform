package com.repair.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.repair.backend.mapper")
public class RepairBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RepairBackendApplication.class, args);
    }

}
