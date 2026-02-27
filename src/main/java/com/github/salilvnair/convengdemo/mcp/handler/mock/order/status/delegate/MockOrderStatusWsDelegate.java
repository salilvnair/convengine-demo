package com.github.salilvnair.convengdemo.mcp.handler.mock.order.status.delegate;

import com.github.salilvnair.convengdemo.config.MockeyApiProperties;
import com.github.salilvnair.convengdemo.mcp.handler.common.AbstractMockeyWsDelegate;
import org.springframework.stereotype.Component;

@Component
public class MockOrderStatusWsDelegate extends AbstractMockeyWsDelegate {

    public MockOrderStatusWsDelegate(MockeyApiProperties mockey) {
        super(mockey);
    }
}
