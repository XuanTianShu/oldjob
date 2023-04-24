package com.yuepei.system.service;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.system.domain.Withdrawal;
import com.yuepei.system.domain.vo.RevenueStatisticsDetailsVo;
import com.yuepei.system.domain.vo.RevenueStatisticsVo;

import java.util.List;

/**
 * @author zzy
 * @date 2023/4/21 09:59
 */
public interface FinanceService {
    List<RevenueStatisticsDetailsVo> investorIncomeStatistics();

    List<RevenueStatisticsDetailsVo> agentIncomeStatistics();

    List<RevenueStatisticsDetailsVo> hospitalIncomeStatistics();

    List<RevenueStatisticsVo> businessStatistics(Long type);

    String withdrawalApplication(Withdrawal withdrawal);
}
