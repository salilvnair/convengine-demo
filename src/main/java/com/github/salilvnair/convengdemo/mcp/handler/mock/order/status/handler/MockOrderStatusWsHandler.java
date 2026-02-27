package com.github.salilvnair.convengdemo.mcp.handler.mock.order.status.handler;

import com.github.salilvnair.convengdemo.mcp.handler.common.AbstractMockeyWsHandler;
import com.github.salilvnair.convengdemo.mcp.handler.mock.order.status.delegate.MockOrderStatusWsDelegate;
import org.springframework.stereotype.Component;

@Component
public class MockOrderStatusWsHandler extends AbstractMockeyWsHandler {

    public MockOrderStatusWsHandler(MockOrderStatusWsDelegate delegate) {
        super(delegate);
    }

    @Override
    public String webServiceName() {
        return "MockOrderStatusMcpHttp";
    }
}
