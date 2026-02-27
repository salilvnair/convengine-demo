package com.github.salilvnair.convengdemo.mcp.handler.mock.order.async.trace.handler;

import com.github.salilvnair.convengdemo.mcp.handler.common.AbstractMockeyWsHandler;
import com.github.salilvnair.convengdemo.mcp.handler.mock.order.async.trace.delegate.MockOrderAsyncTraceWsDelegate;
import org.springframework.stereotype.Component;

@Component
public class MockOrderAsyncTraceWsHandler extends AbstractMockeyWsHandler {

    public MockOrderAsyncTraceWsHandler(MockOrderAsyncTraceWsDelegate delegate) {
        super(delegate);
    }

    @Override
    public String webServiceName() {
        return "MockOrderAsyncTraceMcpHttp";
    }
}
