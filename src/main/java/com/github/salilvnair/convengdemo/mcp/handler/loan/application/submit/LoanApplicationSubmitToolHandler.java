package com.github.salilvnair.convengdemo.mcp.handler.loan.application.submit;

import com.github.salilvnair.api.processor.rest.handler.RestWebServiceHandler;
import com.github.salilvnair.convengdemo.mcp.handler.common.MockeyApiWsContext;
import com.github.salilvnair.convengdemo.mcp.handler.loan.application.submit.handler.LoanApplicationSubmitWsHandler;
import com.github.salilvnair.convengine.engine.mcp.executor.adapter.HttpApiProcessorToolHandler;
import com.github.salilvnair.convengine.engine.mcp.executor.http.ApiProcessorInvocationContext;
import com.github.salilvnair.convengine.engine.session.EngineSession;
import com.github.salilvnair.convengine.entity.CeMcpTool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LoanApplicationSubmitToolHandler implements HttpApiProcessorToolHandler {

    private final LoanApplicationSubmitWsHandler wsHandler;

    @Override
    public String toolCode() {
        return "loan.application.submit";
    }

    @Override
    public RestWebServiceHandler wsHandler(CeMcpTool tool, Map<String, Object> args, EngineSession session) {
        return wsHandler;
    }

    @Override
    public ApiProcessorInvocationContext wsContext(CeMcpTool tool, Map<String, Object> args, EngineSession session) {
        Map<String, Object> safeArgs = args == null ? Map.of() : args;
        MockeyApiWsContext context = new MockeyApiWsContext(safeArgs);
        context.setMethod("POST");
        context.setPath("/api/mock/loan/application/submit");
        context.setBody(Map.of(
                "customerId", safeArgs.getOrDefault("customerId", "CUST-1001"),
                "requestedAmount", safeArgs.getOrDefault("requestedAmount", 350000),
                "tenureMonths", safeArgs.getOrDefault("tenureMonths", 36),
                "applicantName", safeArgs.getOrDefault("applicantName", "Demo Applicant")
        ));
        context.setResponseFieldMap(new LinkedHashMap<>(Map.of(
                "applicationId", "applicationId",
                "status", "status",
                "customerId", "customerId",
                "requestedAmount", "requestedAmount",
                "tenureMonths", "tenureMonths"
        )));
        return context;
    }
}
