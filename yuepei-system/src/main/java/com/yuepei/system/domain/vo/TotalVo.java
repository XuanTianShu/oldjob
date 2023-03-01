package com.yuepei.system.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author zzy
 * @date 2023/3/1 14:25
 */
@Data
public class TotalVo {
    /**订单总金额*/
    private Long orderAmount;

    /**分润总金额*/
    private Long dividendAmount;

    /**有效订单*/
    private int effectiveOrder;

    /**有效订单列表*/
    private List<OrderVo> orderVos;
}
