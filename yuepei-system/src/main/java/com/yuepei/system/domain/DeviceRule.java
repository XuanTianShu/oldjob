package com.yuepei.system.domain;

import com.yuepei.common.annotation.Excel;
import com.yuepei.common.core.domain.BaseEntity;
import lombok.Data;

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
 * @create ：2023/1/12 11:33
 **/
@Data
public class DeviceRule extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 设备类型id */
    @Excel(name = "设备类型id")
    private Long deviceTypeId;

    /** 套餐名称 */
    @Excel(name = "套餐名称")
    private String title;

    /** 规则 */
    @Excel(name = "规则")
    private String rule;

    /** 描述 */
    @Excel(name = "描述")
    private String depict;

    /** 医院id */
    @Excel(name = "医院id")
    private Long hospitalId;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String deviceNumber;
}
