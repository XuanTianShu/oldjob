package com.yuepei.system.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zzy
 * @date 2023/3/1 14:30
 */
@Data
public class UserLeaseOrderVo {

    /** $column.columnComment */
    private Long id;

    /**用户名*/
    private String nickName;

    /**分成比例*/
    private Long proportion;

    /**归属代理商*/
    private String agentName;

    /**归属医院*/
    private String hospitalName;

    /**租借押金*/
    private BigDecimal depositNum;

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
    private Long price;

    /** 支付方式 */
    private String payType;

    /** 实付金额 */
    private Long netAmount;

    /** 优惠券额度 */
    private Long couponPrice;

    /** 订单状态 0-租赁中 1 待支付 2 支付完成 */
    private String status;

    /** 租赁时长 */
    private String playTime;
}
