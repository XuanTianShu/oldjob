package com.yuepei.system.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserDepositVO {

    private Long id;

    private String openid;

    private String orderNumber;

    private String deviceNumber;

    private BigDecimal depositNum;

    private Long depositStatus;

    private Long status;

    private Date createTime;
}
