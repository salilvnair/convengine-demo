package com.github.salilvnair.convengdemo.mcp.handler.mock.customer.profile.handler;

import com.github.salilvnair.convengdemo.mcp.handler.common.AbstractMockeyWsHandler;
import com.github.salilvnair.convengdemo.mcp.handler.mock.customer.profile.delegate.MockCustomerProfileWsDelegate;
import org.springframework.stereotype.Component;

@Component
public class MockCustomerProfileWsHandler extends AbstractMockeyWsHandler {

    public MockCustomerProfileWsHandler(MockCustomerProfileWsDelegate delegate) {
        super(delegate);
    }

    @Override
    public String webServiceName() {
        return "MockCustomerProfileMcpHttp";
    }
}
