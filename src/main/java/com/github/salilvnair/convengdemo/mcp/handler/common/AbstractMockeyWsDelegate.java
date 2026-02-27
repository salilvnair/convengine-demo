package com.github.salilvnair.convengdemo.mcp.handler.common;

import com.github.salilvnair.api.processor.rest.handler.RestWebServiceDelegate;
import com.github.salilvnair.api.processor.rest.model.RestWebServiceRequest;
import com.github.salilvnair.api.processor.rest.model.RestWebServiceResponse;
import com.github.salilvnair.convengdemo.config.MockeyApiProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractMockeyWsDelegate implements RestWebServiceDelegate {

    private final MockeyApiProperties mockey;

    protected AbstractMockeyWsDelegate(MockeyApiProperties mockey) {
        this.mockey = mockey;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RestWebServiceResponse invoke(RestWebServiceRequest request, Map<String, Object> restWsMap, Object... objects) {
        MockeyApiWsRequest wsRequest = (MockeyApiWsRequest) request;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("X-API-KEY", mockey.getApiKey());

        HttpEntity<?> requestEntity = new HttpEntity<>(wsRequest.getBody(), headers);
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = mockey.getBaseUrl() + wsRequest.getPath() + buildQuery(wsRequest.getQueryParams());

        HttpMethod method = HttpMethod.valueOf(
                wsRequest.getMethod() == null ? "GET" : wsRequest.getMethod().toUpperCase()
        );

        ResponseEntity<Map> responseEntity = restTemplate.exchange(apiUrl, method, requestEntity, Map.class);
        Map<String, Object> payload = responseEntity.getBody() == null
                ? new LinkedHashMap<>()
                : new LinkedHashMap<>(responseEntity.getBody());

        MockeyApiWsResponse response = new MockeyApiWsResponse();
        response.setPayload(payload);
        return response;
    }

    private String buildQuery(Map<String, Object> queryParams) {
        if (queryParams == null || queryParams.isEmpty()) {
            return "";
        }
        StringBuilder out = new StringBuilder();
        for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
            if (entry.getKey() == null || entry.getKey().isBlank() || entry.getValue() == null) {
                continue;
            }
            out.append(out.isEmpty() ? "?" : "&")
                    .append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                    .append("=")
                    .append(URLEncoder.encode(String.valueOf(entry.getValue()), StandardCharsets.UTF_8));
        }
        return out.toString();
    }
}
