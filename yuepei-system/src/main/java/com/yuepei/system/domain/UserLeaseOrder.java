package com.yuepei.system.domain;
import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
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
 * @create ：2023/1/9 18:31
 **/
@Data
public class UserLeaseOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 用户唯一标识 */
    private String openid;

    /** 订单编号 */
    private String orderNumber;

    /** 设备编号 */
    private String deviceNumber;

    /** 设备类型 */
    private String deviceType;

    /** 租赁时间 */
    private Date leaseTime;

    /** 租赁地点 */
    private String leaseAddress;

    /** 归还时间 */
    private Date restoreTime;

    /** 归还地点 */
    private String restoreAddress;

    /** 应付金额 */
    private BigDecimal price;

    /** 支付方式 */
    private String payType;

    /** 实付金额 */
    private BigDecimal netAmount;

    /** 优惠券额度 */
    private Long couponPrice;

    /** 订单状态 0-租赁中 1 待支付 2 支付完成 */
    private String status;

    /** 租赁时长 */
    private String playTime;

    /** 支付时间 */
    private Date endTime;

    /** 计费规则 */
    private String rule;

    private String outTradeNo;

    private Long deposit;

    private Long choose;

    private String child;

    private String depositNumber;

    private Long investorProportion;

    private Long hospitalProportion;

    private Long agentProportion;

    private Long platformProportion;

    private Long electric;

    private String deviceMac;

    private String deviceAddress;

    private String rows;

    private String deviceRule;

    private BigDecimal timePrice;

    private BigDecimal fixedPrice;

    private Long isValid;

    private String hospitalId;

    private String investorId;

    private String agentId;
}
