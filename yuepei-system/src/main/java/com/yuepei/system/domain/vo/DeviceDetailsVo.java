package com.yuepei.system.domain.vo;

import lombok.Data;

/**
 * @author zzy
 * @date 2023/2/10 11:44
 */
@Data
public class DeviceDetailsVo {

    /**
     * 医院Id
     * */
    private Long hospitalId;

    /**
     * 医院名称
    * */
    private String hospitalName;

    /**
     * 设备编号
     * */
    private String deviceNumber;

    /**
     * 设备详细地址
     * */
    private String deviceFullAddress;

    /** 设备类型id */
    private Long deviceTypeId;

    /** 设备类型名称 */
    private String deviceTypeName;

    /**设备状态*/
    private Long status;

    /**
     * 设备楼层
     * */
    private String deviceFllor;

    /**
     * 设备科室
     * */
    private String deviceDepartment;

    /**
     * 设备房间
     * */
    private String deviceRoom;

    /**
     * 设备床位号
     * */
    private String deviceBed;
}
