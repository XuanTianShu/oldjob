package com.yuepei.system.domain.vo;

import lombok.Data;

/**
 * @author zzy
 * @date 2023/2/24 17:50
 */
@Data
public class DeviceInfoVo {
    /**设备类型id*/
    private Long deviceTypeId;
    /**设备编号*/
    private String deviceNumber;
    /**设备归属医院id*/
    private Long hospitalId;
    /**设备归属医院名称*/
    private String hospitalName;
    /**
     * 设备楼层
     * */
    private Long deviceFloor;
    /**
     * 设备科室
     * */
    private Long deviceDepartment;
    /**
     * 设备房间
     * */
    private Long deviceRoom;
    /**
     * 设备床位号
     * */
    private Long deviceBed;

}
