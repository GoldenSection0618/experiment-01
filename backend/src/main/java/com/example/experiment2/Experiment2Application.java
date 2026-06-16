package com.example.experiment2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan("com.example.experiment2.mapper")
@SpringBootApplication
public class Experiment2Application {

    public static void main(String[] args) {
        SpringApplication.run(Experiment2Application.class, args);
    }
}
