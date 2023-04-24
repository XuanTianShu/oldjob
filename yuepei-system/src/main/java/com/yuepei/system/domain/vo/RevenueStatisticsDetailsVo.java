package com.yuepei.system.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zzy
 * @date 2023/4/21 11:03
 */
@Data
public class RevenueStatisticsDetailsVo {
    /**用户id*/
    private Long userId;
    /**用户名称*/
    private String nickName;
    /**订单编号*/
    private String orderNumber;
    /**设备编号*/
    private String deviceNumber;
    /**分成比例*/
    private Long proportion;
    /**分成金额*/
    private BigDecimal price;
    /**收益金额*/
    private BigDecimal amount;
    /**优惠券金额*/
    private BigDecimal couponPrice;
    /**创建时间*/
    private Date createTime;
}
