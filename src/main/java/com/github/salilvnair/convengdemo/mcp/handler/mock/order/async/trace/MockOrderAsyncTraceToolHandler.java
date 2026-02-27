package com.github.salilvnair.convengdemo.mcp.handler.mock.order.async.trace;

import com.github.salilvnair.api.processor.rest.handler.RestWebServiceHandler;
import com.github.salilvnair.convengdemo.mcp.handler.common.MockeyApiWsContext;
import com.github.salilvnair.convengdemo.mcp.handler.mock.order.async.trace.handler.MockOrderAsyncTraceWsHandler;
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
public class MockOrderAsyncTraceToolHandler implements HttpApiApiProcessorToolHandler {

    private final MockOrderAsyncTraceWsHandler wsHandler;

    @Override
    public String toolCode() {
        return "mock.order.async.trace";
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
        context.setPath("/api/mock/order/async/trace");
        context.setQueryParams(Map.of("orderId", safeArgs.getOrDefault("orderId", "ORD-7017")));
        context.setResponseFieldMap(new LinkedHashMap<>(Map.of(
                "orderId", "orderId",
                "traceId", "traceId",
                "api4AsyncStatus", "api4AsyncStatus",
                "callbackReceivedAt", "callbackReceivedAt",
                "message", "message"
        )));
        return context;
    }
}
