package com.yuepei.system.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zzy
 * @date 2023/3/7 17:34
 */
@Data
public class IndexVo {
    private String hospitalName;
    private int sum;
    private BigDecimal deviceAmount;
    private Long utilizationRate;
    private Long daytimeUsage;
    private Long nightUsage;
    private Long proportion;

}
