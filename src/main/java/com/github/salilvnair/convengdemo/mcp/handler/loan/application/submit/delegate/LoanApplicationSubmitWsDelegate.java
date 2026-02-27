package com.github.salilvnair.convengdemo.mcp.handler.loan.application.submit.delegate;

import com.github.salilvnair.convengdemo.config.MockeyApiProperties;
import com.github.salilvnair.convengdemo.mcp.handler.common.AbstractMockeyWsDelegate;
import org.springframework.stereotype.Component;

@Component
public class LoanApplicationSubmitWsDelegate extends AbstractMockeyWsDelegate {

    public LoanApplicationSubmitWsDelegate(MockeyApiProperties mockey) {
        super(mockey);
    }
}
