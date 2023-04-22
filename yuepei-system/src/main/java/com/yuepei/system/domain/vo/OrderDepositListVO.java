package com.yuepei.system.domain.vo;

import com.yuepei.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class OrderDepositListVO extends BaseEntity {
    private Long id;

    private String orderNumber;

    private String deviceNumber;

    private String nickName;

    private String phoneNumber;

    private String depositNum;

    private String depositStatus;

    private Date createTime;

    private String status;
}
