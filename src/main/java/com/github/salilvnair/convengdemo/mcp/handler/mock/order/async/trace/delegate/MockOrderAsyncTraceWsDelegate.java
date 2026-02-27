package com.github.salilvnair.convengdemo.mcp.handler.mock.order.async.trace.delegate;

import com.github.salilvnair.convengdemo.config.MockeyApiProperties;
import com.github.salilvnair.convengdemo.mcp.handler.common.AbstractMockeyWsDelegate;
import org.springframework.stereotype.Component;

@Component
public class MockOrderAsyncTraceWsDelegate extends AbstractMockeyWsDelegate {

    public MockOrderAsyncTraceWsDelegate(MockeyApiProperties mockey) {
        super(mockey);
    }
}
