package com.github.salilvnair.convengdemo.mcp.handler.loan.credit.rating.delegate;

import com.github.salilvnair.convengdemo.config.MockeyApiProperties;
import com.github.salilvnair.convengdemo.mcp.handler.common.AbstractMockeyWsDelegate;
import org.springframework.stereotype.Component;

@Component
public class LoanCreditRatingWsDelegate extends AbstractMockeyWsDelegate {

    public LoanCreditRatingWsDelegate(MockeyApiProperties mockey) {
        super(mockey);
    }
}
