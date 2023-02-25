package com.yuepei.system.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author zzy
 * @date 2023/2/21 10:49
 */
@Data
public class FaultVo {
    /**故障编号*/
    private Long faultId;
    /**维修时间*/
    private Date repairTime;
    /**设备编号*/
    private String deviceNumber;
    /**设备地址*/
    private String deviceAddress;
    /**故障类型*/
    private String faultType;
    /**维修状态 0-等待维修 1-维修完成*/
    private int repairStatus;
    /**故障描述*/
    private String remarks;
    /**设备图片*/
    private String devicePicture;
}
