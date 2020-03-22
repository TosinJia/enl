package com.tosin.enl.springcloud.hbase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class HbaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(HbaseApplication.class, args);
    }
}
