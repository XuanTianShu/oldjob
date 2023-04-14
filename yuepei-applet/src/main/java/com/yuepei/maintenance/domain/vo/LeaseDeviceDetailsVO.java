package com.yuepei.maintenance.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class LeaseDeviceDetailsVO {
    private String deviceTypeName;

    private String deviceNumber;

    private String deviceAddress;

    private String hospitalName;

    private Long status;

    private String electric;

    private Date time;

    private String platTime;

    private String deviceFullAddress;
}
