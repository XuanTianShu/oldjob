package com.yuepei.system.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zzy
 * @date 2023/2/13 14:24
 */
@Data
public class GoodsOrder {
    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     * */
    private Long orderId;

    /**
     * 用户id
     * */
    private Long userId;

    /**
     * 商品订单编号
     * */
    private String goodsOrderNumber;

    /**
     * 设备类型id
     * */
    private Long deviceTypeId;

    /**
     * 设备编号
     * */
    private String deviceNumber;

    /**
     * 商品id
     * */
    private Long goodsId;

    /**
     * 商品价格
     * */
    private BigDecimal goodsMoney;

    /**
     * 购买时间
     * */
    private Date creatTime;

    /**
     * 支付金额
     * */
    private BigDecimal paymentMoney;

    /**
     * 医院id
     * */
    private Long hospitalId;

    /**
     * 支付方式
     * */
    private String paymentMethod;

    /**
     * 交易状态
     * */
    private int transactionStatus;
}
