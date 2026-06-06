package com.github.salilvnair.convengdemo.mcp.handler.mock.order.status;

import com.github.salilvnair.api.processor.rest.handler.RestWebServiceHandler;
import com.github.salilvnair.convengdemo.mcp.handler.common.MockeyApiWsContext;
import com.github.salilvnair.convengdemo.mcp.handler.mock.order.status.handler.MockOrderStatusWsHandler;
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
public class MockOrderStatusToolHandler implements HttpApiProcessorToolHandler {

    private final MockOrderStatusWsHandler wsHandler;

    @Override
    public String toolCode() {
        return "mock.order.status";
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
        context.setPath("/api/mock/order/status");
        context.setQueryParams(Map.of("orderId", safeArgs.getOrDefault("orderId", "ORD-7017")));
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
