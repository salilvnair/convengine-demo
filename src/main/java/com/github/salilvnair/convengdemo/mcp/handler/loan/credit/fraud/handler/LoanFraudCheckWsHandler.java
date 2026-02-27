package com.github.salilvnair.convengdemo.mcp.handler.loan.credit.fraud.handler;

import com.github.salilvnair.convengdemo.mcp.handler.common.AbstractMockeyWsHandler;
import com.github.salilvnair.convengdemo.mcp.handler.loan.credit.fraud.delegate.LoanFraudCheckWsDelegate;
import org.springframework.stereotype.Component;

@Component
public class LoanFraudCheckWsHandler extends AbstractMockeyWsHandler {

    public LoanFraudCheckWsHandler(LoanFraudCheckWsDelegate delegate) {
        super(delegate);
    }

    @Override
    public String webServiceName() {
        return "LoanFraudCheckMcpHttp";
    }
}
