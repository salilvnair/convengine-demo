package com.github.salilvnair.convengdemo.mcp.handler.loan.application.submit.handler;

import com.github.salilvnair.convengdemo.mcp.handler.common.AbstractMockeyWsHandler;
import com.github.salilvnair.convengdemo.mcp.handler.loan.application.submit.delegate.LoanApplicationSubmitWsDelegate;
import org.springframework.stereotype.Component;

@Component
public class LoanApplicationSubmitWsHandler extends AbstractMockeyWsHandler {

    public LoanApplicationSubmitWsHandler(LoanApplicationSubmitWsDelegate delegate) {
        super(delegate);
    }

    @Override
    public String webServiceName() {
        return "LoanApplicationSubmitMcpHttp";
    }
}
