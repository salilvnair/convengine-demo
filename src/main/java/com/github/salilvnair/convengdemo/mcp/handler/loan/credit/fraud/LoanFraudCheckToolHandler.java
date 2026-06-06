package com.github.salilvnair.convengdemo.mcp.handler.loan.credit.fraud;

import com.github.salilvnair.api.processor.rest.handler.RestWebServiceHandler;
import com.github.salilvnair.convengdemo.mcp.handler.common.MockeyApiWsContext;
import com.github.salilvnair.convengdemo.mcp.handler.loan.credit.fraud.handler.LoanFraudCheckWsHandler;
import com.github.salilvnair.convengine.engine.agent.executor.adapter.HttpApiProcessorToolHandler;
import com.github.salilvnair.convengine.engine.agent.executor.http.ApiProcessorInvocationContext;
import com.github.salilvnair.convengine.engine.session.EngineSession;
import com.github.salilvnair.convengine.entity.CeAgentTool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LoanFraudCheckToolHandler implements HttpApiProcessorToolHandler {

    private final LoanFraudCheckWsHandler wsHandler;

    @Override
    public String toolCode() {
        return "loan.credit.fraud.check";
    }

    @Override
    public RestWebServiceHandler wsHandler(CeAgentTool tool, Map<String, Object> args, EngineSession session) {
        return wsHandler;
    }

    @Override
    public ApiProcessorInvocationContext wsContext(CeAgentTool tool, Map<String, Object> args, EngineSession session) {
        Map<String, Object> safeArgs = args == null ? Map.of() : args;
        MockeyApiWsContext context = new MockeyApiWsContext(safeArgs);
        context.setMethod("GET");
        context.setPath("/api/mock/loan/credit-card/fraud-check");
        context.setQueryParams(Map.of("customerId", safeArgs.getOrDefault("customerId", "CUST-1001")));
        context.setResponseFieldMap(new LinkedHashMap<>(Map.of(
                "customerId", "customerId",
                "flagged", "flagged",
                "riskLevel", "riskLevel",
                "source", "source"
        )));
        return context;
    }
}
