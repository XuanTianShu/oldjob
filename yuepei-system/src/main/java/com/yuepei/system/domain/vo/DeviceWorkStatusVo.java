package com.yuepei.system.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author zzy
 * @date 2023/2/18 09:47
 */
@Data
public class DeviceWorkStatusVo {

    /**
     * 设备编号
     * */
    private String deviceNumber;

    /** 设备类型id */
    private Long deviceTypeId;

    /** 设备类型名称 */
    private String deviceTypeName;

    /**
     * 设备地址
     * */
    private String deviceAddress;

    /**
     * 设备详细地址
     * */
    private String deviceFullAddress;

    /**工作状态*/
    private Long status;

    /**
     * 电量显示
     * */
    private int power;

    /**心跳时间*/
    private Date heartbeat;

    /**工作累计时间*/
    private String cumulativeTime;

}
