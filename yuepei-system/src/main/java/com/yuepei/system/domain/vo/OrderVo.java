package com.yuepei.system.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zzy
 * @date 2023/2/15 15:37
 */
@Data
public class OrderVo {

    /** 订单编号 */
    private String orderNumber;

    /**医院名称*/
    private String hospitalName;

    /** 实付金额 */
    private BigDecimal netAmount;

    /**收益金额*/
    private BigDecimal incomeAmount;

    /**分润比例*/
    private Long dividendRatio;

}
