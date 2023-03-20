package com.yuepei.investor.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvestorOrderVO {

    private String orderNumber;

    private String deviceNumber;

    private String hospitalName;

    private BigDecimal netAmount;
}
