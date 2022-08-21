package com.negotiation.quiz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

// 这里扫描"com.negotiation.common"是为了读取common中的@Configuration文件配置
@SpringBootApplication(scanBasePackages = {"com.negotiation.quiz", "com.negotiation.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.negotiation.quiz.feign"})
@MapperScan("com.negotiation.quiz.mapper")
public class QuizApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuizApplication.class, args);
    }
}
