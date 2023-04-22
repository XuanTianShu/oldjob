package com.yuepei.system.domain.vo;

import com.yuepei.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConditionOrderVO extends BaseEntity {

    private BigDecimal conditionOrderNum;

    private BigDecimal conditionOrderMoney;
}
