package com.yuepei.system.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderProportionDetail {
    private Long id;

    private String orderNumber;

    private Long userId;

    private Long HospitalId;

    private Long parentId;

    private String proportion;

    private BigDecimal price;
}
