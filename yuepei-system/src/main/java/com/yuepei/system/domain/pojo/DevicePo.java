package com.yuepei.system.domain.pojo;

import com.yuepei.common.annotation.Excel;
import com.yuepei.system.domain.DeviceRule;
import com.yuepei.system.domain.DeviceType;
import lombok.Data;

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
 * @create ：2023/1/12 11:29
 **/
@Data
public class DevicePo {
    /** 设备id */
    private Long deviceId;

    /** 关联设备类型id */
    private Long deviceTypeId;

    /** 设备编号 */
    private String deviceNumber;

    /** 设备编号 */
    private String deviceDeposit;

    /** 关联计费id */
    private Long billingId;

    /** 关联计费id */
    private Long hospitalId;

    /** 设备二维码 */
    private String deviceQrCode;

    /** 设备地址 */
    private String deviceAddress;

    /** 设备物联网地址 */
    private String deviceMac;

    /** 物联网卡号 */
    private String deviceIotNumber;


    /** 设备详细地址 */
    private String deviceFullAddress;

    /** 设备 信息详细 */
    private String rows;

    /** 心跳时间 */
    private Long heartTime;

    /** 设备状态 */
    private Long status;


}
