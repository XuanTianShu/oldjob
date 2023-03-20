package com.yuepei.investor.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class InvestorRevenueVO {
    private BigDecimal revenue;

    private Long orderCount;

    private List<InvestorOrderVO> investorOrderVOList;
}
