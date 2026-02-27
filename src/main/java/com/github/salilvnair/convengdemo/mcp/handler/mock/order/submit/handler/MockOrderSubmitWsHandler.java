package com.github.salilvnair.convengdemo.mcp.handler.mock.order.submit.handler;

import com.github.salilvnair.convengdemo.mcp.handler.common.AbstractMockeyWsHandler;
import com.github.salilvnair.convengdemo.mcp.handler.mock.order.submit.delegate.MockOrderSubmitWsDelegate;
import org.springframework.stereotype.Component;

@Component
public class MockOrderSubmitWsHandler extends AbstractMockeyWsHandler {

    public MockOrderSubmitWsHandler(MockOrderSubmitWsDelegate delegate) {
        super(delegate);
    }

    @Override
    public String webServiceName() {
        return "MockOrderSubmitMcpHttp";
    }
}
