package com.github.salilvnair.convengdemo.mcp.handler.mock.order.submit;

import com.github.salilvnair.api.processor.rest.handler.RestWebServiceHandler;
import com.github.salilvnair.convengdemo.mcp.handler.common.MockeyApiWsContext;
import com.github.salilvnair.convengdemo.mcp.handler.mock.order.submit.handler.MockOrderSubmitWsHandler;
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
public class MockOrderSubmitToolHandler implements HttpApiApiProcessorToolHandler {

    private final MockOrderSubmitWsHandler wsHandler;

    @Override
    public String toolCode() {
        return "mock.order.submit";
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
        context.setPath("/api/mock/order/submit");
        context.setBody(Map.of(
                "orderId", safeArgs.getOrDefault("orderId", "ORD-7017"),
                "customerId", safeArgs.getOrDefault("customerId", "CUST-1001"),
                "submittedByRole", safeArgs.getOrDefault("submittedByRole", "ADMIN"),
                "sourceCity", safeArgs.getOrDefault("sourceCity", "Mumbai"),
                "targetCity", safeArgs.getOrDefault("targetCity", "Bengaluru")
        ));
        context.setResponseFieldMap(new LinkedHashMap<>(Map.of(
                "orderId", "orderId",
                "status", "status",
                "api3Status", "api3Status",
                "api4AsyncStatus", "api4AsyncStatus",
                "message", "message"
        )));
        return context;
    }
}
