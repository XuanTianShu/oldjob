package com.yuepei.system.domain;

import com.yuepei.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

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
 * @create ：2022/12/5 15:13
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class Order extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 订单编号 */
    private String orderNumber;

    /** 用户id */
    private Long userId;

    /** 设备编号 */
    private String deviceNumber;

    /** 支付方式 - 0 余额 1-微信 2-支付宝 */
    private String payType;

    /** 支付时间 */
    private Date payTime;

    /** 支付金额 */
    private String payAmount;

    /** 优惠金额 */
    private String couponAmount;

    /** 实付金额 */
    private String netAmount;

    /** 支付详情 */
    private String payRows;

    /** 订单状态 -0已支付 - 1 未支付  2- 待支付*/
    private String status;

    /** 抽单状态 -0显示 - 1 隐藏（抽单）  */
    private String rackStatus;
}
