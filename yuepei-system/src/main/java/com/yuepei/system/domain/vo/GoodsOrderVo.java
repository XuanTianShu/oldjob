package com.yuepei.system.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zzy
 * @date 2023/2/14 09:51
 */
@Data
public class GoodsOrderVo {

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
     * 设备类型名称
     * */
    private String deviceTypeName;

    /**
     * 设备编号
     * */
    private String deviceNumber;

    /**
     * 商品id
     * */
    private Long goodsId;

    /**
     * 商品名称
     * */
    private String goodsName;

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
     * 医院名称
     * */
    private String hospitalName;

    /**
     * 支付方式
     * */
    private String paymentMethod;

    /**
     * 交易状态
     * */
    private int transactionStatus;
}
