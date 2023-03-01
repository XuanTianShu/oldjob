package com.yuepei.system.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author zzy
 * @date 2023/2/15 15:37
 */
@Data
public class OrderVo {

    /** 订单编号 */
    private String orderNumber;

    /** 实付金额 */
    private Long netAmount;

    /**收益金额*/
    private Long incomeAmount;

    /**分润比例*/
    private Long dividendRatio;

}
