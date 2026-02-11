package com.github.salilvnair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(
        basePackages = {"com.github.salilvnair.convengdemo"}
)
@EntityScan(
        basePackages = {"com.github.salilvnair.convengdemo.entity"}
)
@EnableJpaRepositories(
        basePackages = {"com.github.salilvnair.convengdemo.repo"}
)
public class ConvengineDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConvengineDemoApplication.class, args);
    }

}
