package com.yuepei.maintenance.domain.vo;

import lombok.Data;

@Data
public class StockVO {
    private String deviceNumber;

    private String deviceTypeId;

    private String hospitalName;

    private String deviceAddress;

    private String status;

    private String rows;

    private String deviceTypeName;
}
