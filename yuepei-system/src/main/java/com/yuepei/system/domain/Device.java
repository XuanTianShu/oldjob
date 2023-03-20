package com.yuepei.system.domain;

import com.yuepei.common.annotation.Excel;
import com.yuepei.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 　　　　 ┏┓       ┏┓+ +
 * 　　　　┏┛┻━━━━━━━┛┻┓ + +
 * 　　　　┃　　　　　　 ┃
 * 　　　　┃　　　━　　　┃ ++ + + +
 * 　　　 █████━█████  ┃+
 * 　　　　┃　　　　　　 ┃ +
 * 　　　　┃　　　┻　　　┃
 * 　　　　┃　　　　　　 ┃ + +
 * 　　　　┗━━┓　　　 ┏━┛
 * 　　　　　　┃　　  ┃
 * 　　　　　　┃　　  ┃ + + + +
 * 　　　　　　┃　　　┃　Code is far away from bug with the animal protection
 * 　　　　　　┃　　　┃ + 　　　　         神兽保佑,代码永无bug
 * 　　　　　　┃　　　┃
 * 　　　　　　┃　　　┃　　+
 * 　　　　　　┃　 　 ┗━━━┓ + +
 * 　　　　　　┃ 　　　　　┣-┓
 * 　　　　　　┃ 　　　　　┏-┛
 * 　　　　　　┗┓┓┏━━━┳┓┏┛ + + + +
 * 　　　　　　 ┃┫┫　 ┃┫┫
 * 　　　　　　 ┗┻┛　 ┗┻┛+ + + +
 * *********************************************************
 *
 * @author ：AK
 * @create ：2022/11/14 14:10
 **/
@Data
public class Device extends BaseEntity {
    private static final long serialVersionUID = 1L;


    /** 设备id */
    private Long deviceId;

    /** 关联设备类型id */
//    @Excel(name = "关联设备类型id")
    private Long deviceTypeId;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String deviceNumber;

    /** 设备编号 */
    @Excel(name = "设备押金")
    private String deviceDeposit;

    /** 关联计费id */
//    @Excel(name = "关联计费id")
    private Long billingId;

    /** 关联计费id */
//    @Excel(name = "关联医院id")
    private Long hospitalId;

    /** 设备二维码 */
//    @Excel(name = "设备二维码")
    private String deviceQrCode;

    /** 设备地址 */
    @Excel(name = "设备地址")
    private String deviceAddress;

    /** 设备物联网地址 */
    @Excel(name = "MAC")
    private String deviceMac;

    /** 物联网卡号 */
    @Excel(name = "物联网卡号")
    private String deviceIotNumber;


    /** 设备详细地址 */
    @Excel(name = "设备详细地址")
    private String deviceFullAddress;

    /** 设备 信息详细 */
    private String rows;

    /** 心跳时间 */
    @Excel(name = "心跳时间")
    private Long heartTime;

    /** 设备状态 */
    @Excel(name = "设备状态")
    private Long status;

    /** 设备电量 */
    @Excel(name = "设备电量")
    private Long electric;

    /** 预警值 */
    @Excel(name = "预警值")
    private Long electricEarly;

    private String content;

    private String investorId;

    private List<DeviceRule> deviceRules;
}
