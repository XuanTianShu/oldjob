package com.yuepei.system.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class RechargeVO {
    private Long id;

    private String openid;

    private String outTradeNo;

    private String price;

    private Date endTime;

    private String status;

    private String nickName;

    private String phoneNumber;
}
