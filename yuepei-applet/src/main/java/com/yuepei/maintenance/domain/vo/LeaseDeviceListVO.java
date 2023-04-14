package com.yuepei.maintenance.domain.vo;

import lombok.Data;

@Data
public class LeaseDeviceListVO {
    private String deviceTypeName;

    private String deviceNumber;

    private String deviceAddress;

    private Long status;
}
