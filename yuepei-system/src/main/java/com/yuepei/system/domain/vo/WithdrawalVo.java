package com.yuepei.system.domain.vo;

import com.yuepei.system.domain.Withdrawal;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zzy
 * @date 2023/4/25 15:38
 */
@Data
public class WithdrawalVo {
    /**昨天*/
    private BigDecimal yesterday;
    /**今天*/
    private BigDecimal today;
    /**本月*/
    private BigDecimal month;
    /**累计*/
    private BigDecimal accumulate;
    private List<Withdrawal> withdrawalList;
}
