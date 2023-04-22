package com.yuepei.system.mapper;

import com.yuepei.system.domain.Withdrawal;
import com.yuepei.system.domain.vo.RevenueStatisticsDetailsVo;
import com.yuepei.system.domain.vo.RevenueStatisticsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zzy
 * @date 2023/4/21 10:02
 */
public interface FinanceServiceMapper {
    List<RevenueStatisticsDetailsVo> selectRevenueStatistics();

    List<RevenueStatisticsDetailsVo> selectAgentIncomeStatistics();

    List<RevenueStatisticsDetailsVo> selectHospitalIncomeStatistics();

    List<RevenueStatisticsVo> selectStatisticsByYear();

    List<RevenueStatisticsVo> selectStatisticsByMonth();

    List<RevenueStatisticsVo> selectStatisticsByDay();

    List<RevenueStatisticsVo> selectStatisticsByInvestor();

    List<RevenueStatisticsVo> selectStatisticsByAgent();

    List<RevenueStatisticsVo> selectStatisticsByHospital();

    List<RevenueStatisticsVo> selectStatisticsByPlatformHospital();

    void insertWithdrawalApplication(Withdrawal withdrawal);
}
