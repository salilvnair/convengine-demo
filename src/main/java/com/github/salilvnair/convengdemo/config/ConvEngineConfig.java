package com.github.salilvnair.convengdemo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.salilvnair.api.processor.rest.facade.RestWebServiceFacade;
import com.github.salilvnair.ccf.annotation.EnableCcfCore;
import com.github.salilvnair.convengine.annotation.EnableConvEngine;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableConvEngine
@EnableCcfCore
public class ConvEngineConfig {
    @Bean
    public RestWebServiceFacade restWebServiceFacade() {
        return new RestWebServiceFacade();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
