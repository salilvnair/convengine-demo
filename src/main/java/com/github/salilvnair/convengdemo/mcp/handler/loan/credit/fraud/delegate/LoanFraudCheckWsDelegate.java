package com.github.salilvnair.convengdemo.mcp.handler.loan.credit.fraud.delegate;

import com.github.salilvnair.convengdemo.config.MockeyApiProperties;
import com.github.salilvnair.convengdemo.mcp.handler.common.AbstractMockeyWsDelegate;
import org.springframework.stereotype.Component;

@Component
public class LoanFraudCheckWsDelegate extends AbstractMockeyWsDelegate {

    public LoanFraudCheckWsDelegate(MockeyApiProperties mockey) {
        super(mockey);
    }
}
