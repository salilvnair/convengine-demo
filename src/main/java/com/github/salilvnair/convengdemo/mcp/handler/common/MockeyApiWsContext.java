package com.github.salilvnair.convengdemo.mcp.handler.common;

import com.github.salilvnair.convengine.engine.mcp.executor.http.ApiProcessorInvocationContext;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class MockeyApiWsContext extends ApiProcessorInvocationContext {

    private String method;
    private String path;
    private Map<String, Object> queryParams = new LinkedHashMap<>();
    private Object body;
    private Map<String, String> responseFieldMap = new LinkedHashMap<>();

    public MockeyApiWsContext(Map<String, Object> inputArgs) {
        super(inputArgs);
    }
}
