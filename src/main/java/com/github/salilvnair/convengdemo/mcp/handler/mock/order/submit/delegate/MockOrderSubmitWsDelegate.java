package com.github.salilvnair.convengdemo.mcp.handler.mock.order.submit.delegate;

import com.github.salilvnair.convengdemo.config.MockeyApiProperties;
import com.github.salilvnair.convengdemo.mcp.handler.common.AbstractMockeyWsDelegate;
import org.springframework.stereotype.Component;

@Component
public class MockOrderSubmitWsDelegate extends AbstractMockeyWsDelegate {

    public MockOrderSubmitWsDelegate(MockeyApiProperties mockey) {
        super(mockey);
    }
}
