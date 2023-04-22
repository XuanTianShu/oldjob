package com.yuepei.system.domain.vo;

import com.yuepei.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderSumAndMoneyVO extends BaseEntity {

    /** 今日订单数量 */
    private BigDecimal todayOrderNum;

    /** 今日订单总收入 */
    private BigDecimal todayOrderMoney;

    /** 近七天订单数量 */
    private BigDecimal sevenDayOrderNum;

    /** 近七天订单总收入 */
    private BigDecimal sevenDayOrderMoney;

    /** 近三十天订单数量 */
    private BigDecimal thirtyDayOrderNum;

    /** 近三十天订单总收入 */
    private BigDecimal thirtyDayOrderMoney;
}
