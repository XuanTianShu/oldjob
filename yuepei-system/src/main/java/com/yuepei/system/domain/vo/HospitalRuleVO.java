package com.yuepei.system.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HospitalRuleVO {
    private static final long serialVersionUID = 1L;

    private Long time;

    private BigDecimal price;

    private String startTime;

    private String endTime;
}
