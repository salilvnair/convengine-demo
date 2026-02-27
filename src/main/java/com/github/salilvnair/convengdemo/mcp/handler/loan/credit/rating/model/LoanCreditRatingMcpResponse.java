package com.github.salilvnair.convengdemo.mcp.handler.loan.credit.rating.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanCreditRatingMcpResponse {
    private String customerId;
    private Integer creditRating;
    private Boolean eligibleForFraudCheck;
    private String ratingProvider;
}
