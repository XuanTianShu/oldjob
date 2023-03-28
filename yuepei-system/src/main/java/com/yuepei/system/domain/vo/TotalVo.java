package com.yuepei.system.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zzy
 * @date 2023/3/1 14:25
 */
@Data
public class TotalVo {
    /**订单总金额*/
    private BigDecimal orderAmount;

    /**分润总金额*/
    private BigDecimal dividendAmount;

    /**有效订单*/
    private Integer effectiveOrder;

    /**有效订单列表*/
    private List<OrderVo> orderVos;
}
