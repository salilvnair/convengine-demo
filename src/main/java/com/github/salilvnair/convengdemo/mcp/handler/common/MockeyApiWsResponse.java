package com.github.salilvnair.convengdemo.mcp.handler.common;

import com.github.salilvnair.api.processor.rest.model.RestWebServiceResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class MockeyApiWsResponse implements RestWebServiceResponse {
    private Map<String, Object> payload = new LinkedHashMap<>();
}
