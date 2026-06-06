package com.github.salilvnair.convengdemo.mcp.handler.loan.debt.summary;

import com.github.salilvnair.convengdemo.config.MockeyApiProperties;
import com.github.salilvnair.convengine.engine.agent.executor.adapter.HttpApiRequestingToolHandler;
import com.github.salilvnair.convengine.engine.agent.executor.http.HttpApiAuthSpec;
import com.github.salilvnair.convengine.engine.agent.executor.http.HttpApiAuthType;
import com.github.salilvnair.convengine.engine.agent.executor.http.HttpApiRequestSpec;
import com.github.salilvnair.convengine.engine.agent.executor.http.HttpApiResponseMapping;
import com.github.salilvnair.convengine.engine.agent.executor.http.HttpApiResponseMappingMode;
import com.github.salilvnair.convengine.engine.session.EngineSession;
import com.github.salilvnair.convengine.entity.CeAgentTool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class LoanDebtSummaryToolHandler implements HttpApiRequestingToolHandler {

    private final MockeyApiProperties mockey;

    @Override
    public String toolCode() {
        return "loan.debt.credit.summary";
    }

    @Override
    public HttpApiRequestSpec requestSpec(CeAgentTool tool, Map<String, Object> args, EngineSession session) {
        Map<String, Object> safeArgs = args == null ? Map.of() : args;
        return new HttpApiRequestSpec(
                "GET",
                mockey.getBaseUrl() + "/api/mock/loan/debt-credit/summary",
                Map.of("Accept", "application/json"),
                Map.of("customerId", safeArgs.getOrDefault("customerId", "CUST-1001")),
                null,
                new HttpApiAuthSpec(HttpApiAuthType.API_KEY, "X-API-KEY", mockey.getApiKey(), null),
                null,
                new HttpApiResponseMapping(
                        HttpApiResponseMappingMode.FIELD_TEMPLATE,
                        null,
                        Map.of(
                                "customerId", "$.customerId",
                                "monthlyDebt", "$.monthlyDebt",
                                "monthlyIncome", "$.monthlyIncome",
                                "dti", "$.dti",
                                "availableCredit", "$.availableCredit")));
    }
}
