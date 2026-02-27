package com.github.salilvnair.convengdemo.mcp.handler.mock.customer.profile.delegate;

import com.github.salilvnair.convengdemo.config.MockeyApiProperties;
import com.github.salilvnair.convengdemo.mcp.handler.common.AbstractMockeyWsDelegate;
import org.springframework.stereotype.Component;

@Component
public class MockCustomerProfileWsDelegate extends AbstractMockeyWsDelegate {

    public MockCustomerProfileWsDelegate(MockeyApiProperties mockey) {
        super(mockey);
    }
}
