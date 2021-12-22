package com.lee.mht;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lee.mht.*.dao")
public class MhtApplication {

    public static void main(String[] args) {
        SpringApplication.run(MhtApplication.class, args);
    }

}
