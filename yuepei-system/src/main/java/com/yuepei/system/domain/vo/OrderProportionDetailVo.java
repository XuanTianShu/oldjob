package com.yuepei.system.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zzy
 * @date 2023/4/12 17:28
 */
@Data
public class OrderProportionDetailVo {
    private Long id;

    private String orderNumber;

    private Long userId;

    private String proportion;

    private Date leaseTime;

    private BigDecimal netAmount;

    private String hospitalId;
}
