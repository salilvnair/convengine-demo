package com.github.salilvnair.convengdemo.mcp.handler.loan.credit.rating;

import com.github.salilvnair.api.processor.rest.handler.RestWebServiceHandler;
import com.github.salilvnair.convengdemo.mcp.handler.common.MockeyApiWsContext;
import com.github.salilvnair.convengdemo.mcp.handler.loan.credit.rating.handler.LoanCreditRatingWsHandler;
import com.github.salilvnair.convengdemo.mcp.handler.loan.credit.rating.model.LoanCreditRatingMcpResponse;
import com.github.salilvnair.convengine.engine.mcp.executor.adapter.HttpApiApiProcessorToolHandler;
import com.github.salilvnair.convengine.engine.mcp.executor.http.ApiProcessorInvocationContext;
import com.github.salilvnair.convengine.engine.session.EngineSession;
import com.github.salilvnair.convengine.entity.CeMcpTool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LoanCreditRatingToolHandler implements HttpApiApiProcessorToolHandler {

    private final LoanCreditRatingWsHandler wsHandler;

    @Override
    public String toolCode() {
        return "loan.credit.rating.check";
    }

    @Override
    public RestWebServiceHandler wsHandler(CeMcpTool tool, Map<String, Object> args, EngineSession session) {
        return wsHandler;
    }

    @Override
    public ApiProcessorInvocationContext wsContext(CeMcpTool tool, Map<String, Object> args, EngineSession session) {
        Map<String, Object> safeArgs = args == null ? Map.of() : args;
        MockeyApiWsContext context = new MockeyApiWsContext(safeArgs);
        context.setMethod("GET");
        context.setPath("/api/mock/loan/credit-union/rating");
        context.setQueryParams(Map.of("customerId", safeArgs.getOrDefault("customerId", "CUST-1001")));
        context.setResponseFieldMap(new LinkedHashMap<>(Map.of(
                "customerId", "customerId",
                "creditRating", "creditRating",
                "eligibleForFraudCheck", "eligibleForFraudCheck",
                "ratingProvider", "ratingProvider"
        )));
        return context;
    }

    @Override
    public Class<?> responseMapperClass(CeMcpTool tool, Map<String, Object> args, EngineSession session) {
        return LoanCreditRatingMcpResponse.class;
    }
}
