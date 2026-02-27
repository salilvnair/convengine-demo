package com.github.salilvnair.convengdemo.mcp.handler.common;

import com.github.salilvnair.api.processor.rest.model.RestWebServiceRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class MockeyApiWsRequest implements RestWebServiceRequest {
    private String method;
    private String path;
    private Map<String, Object> queryParams = new LinkedHashMap<>();
    private Object body;
}
