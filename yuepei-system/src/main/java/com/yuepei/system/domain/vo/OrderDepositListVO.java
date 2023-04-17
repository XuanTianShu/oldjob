package com.yuepei.system.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDepositListVO {
    private Long id;

    private String orderNumber;

    private String deviceNumber;

    private String nickName;

    private String phoneNumber;

    private String depositNum;

    private String depositStatus;

    private Date createTime;
}
