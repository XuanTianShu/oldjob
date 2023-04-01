package com.yuepei.system.domain;

import lombok.Data;

@Data
public class OrderProportionDetail {
    private Long id;

    private String orderNumber;

    private Long userId;

    private String proportion;
}
