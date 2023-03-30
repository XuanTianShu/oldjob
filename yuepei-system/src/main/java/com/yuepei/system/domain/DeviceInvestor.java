package com.yuepei.system.domain;

import lombok.Data;

import java.util.Date;

@Data
public class DeviceInvestor {

    private Long id;

    private String deviceNumber;

    private Long investorId;

    private String proportion;

    private Date createTime;
}
