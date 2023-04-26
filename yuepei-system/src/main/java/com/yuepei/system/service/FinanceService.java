package com.yuepei.system.service;

import com.yuepei.system.domain.Withdrawal;
import com.yuepei.system.domain.vo.RevenueStatisticsDetailsVo;
import com.yuepei.system.domain.vo.RevenueStatisticsVo;
import com.yuepei.system.domain.vo.SelectListVo;
import com.yuepei.system.domain.vo.WithdrawalVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zzy
 * @date 2023/4/21 09:59
 */
public interface FinanceService {
    List<RevenueStatisticsDetailsVo> investorIncomeStatistics(Long investorId, String startTime, String endTime);

    List<RevenueStatisticsDetailsVo> agentIncomeStatistics(Long agentId, String startTime, String endTime);

    List<RevenueStatisticsDetailsVo> hospitalIncomeStatistics(Long hospitalId, String startTime, String endTime);

    List<RevenueStatisticsDetailsVo> deviceStatistics(String deviceNumber, String startTime, String endTime);

    List<RevenueStatisticsVo> businessStatistics(Long type, String payType,String agentId, String hospitalId, String deviceNumber, String startTime, String endTime);

    String withdrawalApplication(Withdrawal withdrawal);

    String byRejecting(String orderNumber, int type);

    List<Withdrawal> roleWithdrawalStatistics(String role, Long userId, Long status, String startApplyTime, String endApplyTime, String startHandleTime, String endHandleTime);

    WithdrawalVo withdrawalStatistics(String orderNumber);

    List<SelectListVo> selectInvestor();

    List<SelectListVo> selectAgent();

    List<SelectListVo> selectHospital();

    List<String> selectDevice();

    List<Withdrawal> selectWithdrawalList();
}
