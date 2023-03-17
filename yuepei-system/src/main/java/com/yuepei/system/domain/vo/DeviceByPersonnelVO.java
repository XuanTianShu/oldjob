package com.yuepei.system.domain.vo;

import com.yuepei.common.annotation.Excel;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.system.domain.DeviceRule;
import lombok.Data;

import java.util.List;

@Data
public class DeviceByPersonnelVO extends BaseController {
    private static final long serialVersionUID = 1L;


    /** 设备id */
    private Long deviceId;

    /** 关联设备类型id */
    @Excel(name = "关联设备类型id")
    private Long deviceTypeId;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String deviceNumber;

    /** 关联计费id */
    @Excel(name = "关联医院id")
    private Long hospitalId;

    /** 设备二维码 */
    @Excel(name = "设备二维码")
    private String deviceQrCode;

    /** 设备地址 */
    @Excel(name = "设备地址")
    private String deviceAddress;

    /** 设备物联网地址 */
    @Excel(name = "MAC")
    private String deviceMac;

    /** 设备详细地址 */
    @Excel(name = "设备详细地址")
    private String deviceFullAddress;

    /** 设备 信息详细 */
    private String rows;

    /** 设备状态 */
    @Excel(name = "设备状态")
    private Long status;

    /** 设备电量 */
    @Excel(name = "设备电量")
    private Long electric;

    /** 预警值 */
    @Excel(name = "预警值")
    private Long electricEarly;

    private String investorId;

    private String agentName;

    private String hospitalName;

    private String content;
}
