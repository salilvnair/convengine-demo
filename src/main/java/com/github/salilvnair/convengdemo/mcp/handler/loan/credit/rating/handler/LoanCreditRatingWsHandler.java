package com.github.salilvnair.convengdemo.mcp.handler.loan.credit.rating.handler;

import com.github.salilvnair.convengdemo.mcp.handler.common.AbstractMockeyWsHandler;
import com.github.salilvnair.convengdemo.mcp.handler.loan.credit.rating.delegate.LoanCreditRatingWsDelegate;
import org.springframework.stereotype.Component;

@Component
public class LoanCreditRatingWsHandler extends AbstractMockeyWsHandler {

    public LoanCreditRatingWsHandler(LoanCreditRatingWsDelegate delegate) {
        super(delegate);
    }

    @Override
    public String webServiceName() {
        return "LoanCreditRatingMcpHttp";
    }
}
