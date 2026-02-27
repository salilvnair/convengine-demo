package com.github.salilvnair.convengdemo.mcp.handler.common;

import com.github.salilvnair.api.processor.rest.handler.RestWebServiceDelegate;
import com.github.salilvnair.api.processor.rest.handler.RestWebServiceHandler;
import com.github.salilvnair.api.processor.rest.model.RestWebServiceRequest;
import com.github.salilvnair.api.processor.rest.model.RestWebServiceResponse;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractMockeyWsHandler implements RestWebServiceHandler {

    private final RestWebServiceDelegate delegate;

    protected AbstractMockeyWsHandler(RestWebServiceDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public RestWebServiceDelegate delegate() {
        return delegate;
    }

    @Override
    public RestWebServiceRequest prepareRequest(Map<String, Object> restWsMap, Object... objects) {
        MockeyApiWsContext context = (MockeyApiWsContext) objects[0];
        MockeyApiWsRequest request = new MockeyApiWsRequest();
        request.setMethod(context.getMethod());
        request.setPath(context.getPath());
        request.setQueryParams(context.getQueryParams());
        request.setBody(context.getBody());
        return request;
    }

    @Override
    public void processResponse(
            RestWebServiceRequest request,
            RestWebServiceResponse response,
            Map<String, Object> restWsMap,
            Object... objects
    ) {
        MockeyApiWsContext context = (MockeyApiWsContext) objects[0];
        MockeyApiWsResponse wsResponse = (MockeyApiWsResponse) response;
        Map<String, Object> payload = wsResponse == null || wsResponse.getPayload() == null
                ? Map.of()
                : wsResponse.getPayload();

        Map<String, Object> mapped = new LinkedHashMap<>();
        Map<String, String> responseFieldMap = context.getResponseFieldMap();
        if (responseFieldMap == null || responseFieldMap.isEmpty()) {
            mapped.putAll(payload);
        } else {
            for (Map.Entry<String, String> entry : responseFieldMap.entrySet()) {
                if (entry.getKey() == null || entry.getKey().isBlank()) {
                    continue;
                }
                String sourceKey = entry.getValue();
                mapped.put(entry.getKey(), sourceKey == null || sourceKey.isBlank() ? null : payload.get(sourceKey));
            }
        }

        context.setMappedResponse(mapped);
        restWsMap.put("mappedResponse", mapped);
    }
}
