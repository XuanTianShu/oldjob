package com.yuepei.system.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author zzy
 * @date 2023/4/21 10:34
 */
@Data
public class RevenueStatisticsVo {
    /**营业金额*/
    private BigDecimal businessAmount;
    /**实收金额*/
    private BigDecimal paidAmount;
    /**优惠金额*/
    private BigDecimal discountAmount;
    /**平台分成*/
    private BigDecimal platformDivision;
    /**投资商分成*/
    private BigDecimal investorDivision;
    /**代理商分成*/
    private BigDecimal agentDivision;
    /**医院分成*/
    private BigDecimal hospitalDivision;
    /**订单数量*/
    private int orderSum;
    /**统计类别*/
    private String nickName;
    /**用户id*/
    private Long userId;
    /**支付方式*/
    private int payType;
    /**代理商id*/
    private Long agentId;
    /**医院id*/
    private Long hospitalId;
    /**设备编号*/
    private String deviceNumber;
}
