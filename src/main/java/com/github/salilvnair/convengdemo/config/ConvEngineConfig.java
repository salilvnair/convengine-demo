package com.github.salilvnair.convengdemo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.salilvnair.api.processor.rest.facade.RestWebServiceFacade;
import com.github.salilvnair.ccf.annotation.EnableCcfCore;
import com.github.salilvnair.convengine.annotation.EnableConvEngine;
import com.github.salilvnair.convengine.annotation.EnableConvEngineAsyncAuditDispatch;
import com.github.salilvnair.convengine.annotation.EnableConvEngineAsyncConversation;
import com.github.salilvnair.convengine.annotation.EnableConvEngineCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConvEngine
@EnableCcfCore
@EnableConvEngineCaching
@EnableConvEngineAsyncConversation
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
