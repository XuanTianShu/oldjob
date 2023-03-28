package com.yuepei.system.domain.vo;

import com.yuepei.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class LeaseOrderVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    private String openid;

    /** 用户名称 */
    private String nickName;

    /** 设备类型编号 */
    private Long deviceTypeId;

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

    /** 归属医院 */
    private String hospitalName;

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
    private BigDecimal couponPrice;

    /** 订单状态 0-租赁中 1 待支付 2 支付完成 */
    private Integer status;

    /** 租赁时长 */
    private String playTime;

    /** 支付流水号 */
    private String outTradeNo;

    /** 代理商名称 */
    private String agentName;

    private Long investorProportion;

    private Long hospitalProportion;

    private Long agentProportion;

    private Long platformProportion;

    private Long choose;

    private String child;

    private String phoneNumber;

}
