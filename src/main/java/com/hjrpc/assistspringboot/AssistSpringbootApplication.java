package com.hjrpc.assistspringboot;

import com.hjrpc.assistspringboot.annoation.EnableDict;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDict(value = "com.hjrpc.assistspringboot.dto")
public class AssistSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssistSpringbootApplication.class, args);
    }
}
