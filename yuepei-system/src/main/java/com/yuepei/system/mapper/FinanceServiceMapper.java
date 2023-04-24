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

    List<RevenueStatisticsDetailsVo> selectDeviceStatistics();

    List<RevenueStatisticsVo> selectStatisticsByYear(@Param("payType") String payType,@Param("agentId") String agentId,@Param("hospitalId") String hospitalId,@Param("deviceNumber") String deviceNumber,@Param("startTime") String startTime,@Param("endTime") String endTime);

    List<RevenueStatisticsVo> selectStatisticsByMonth(@Param("payType") String payType,@Param("agentId") String agentId,@Param("hospitalId") String hospitalId,@Param("deviceNumber") String deviceNumber,@Param("startTime") String startTime,@Param("endTime") String endTime);

    List<RevenueStatisticsVo> selectStatisticsByDay(@Param("payType") String payType,@Param("agentId") String agentId,@Param("hospitalId") String hospitalId,@Param("deviceNumber") String deviceNumber,@Param("startTime") String startTime,@Param("endTime") String endTime);

    List<RevenueStatisticsVo> selectStatisticsByInvestor(@Param("payType") String payType,@Param("agentId") String agentId,@Param("hospitalId") String hospitalId,@Param("deviceNumber") String deviceNumber,@Param("startTime") String startTime,@Param("endTime") String endTime);

    List<RevenueStatisticsVo> selectStatisticsByAgent(@Param("payType") String payType,@Param("agentId") String agentId,@Param("hospitalId") String hospitalId,@Param("deviceNumber") String deviceNumber,@Param("startTime") String startTime,@Param("endTime") String endTime);

    List<RevenueStatisticsVo> selectStatisticsByHospital(@Param("payType") String payType,@Param("agentId") String agentId,@Param("hospitalId") String hospitalId,@Param("deviceNumber") String deviceNumber,@Param("startTime") String startTime,@Param("endTime") String endTime);

    List<RevenueStatisticsVo> selectStatisticsByPlatformHospital(@Param("payType") String payType,@Param("agentId") String agentId,@Param("hospitalId") String hospitalId,@Param("deviceNumber") String deviceNumber,@Param("startTime") String startTime,@Param("endTime") String endTime);

    void insertWithdrawalApplication(Withdrawal withdrawal);

    List<Withdrawal> selectWithdrawalStatistics(@Param("userId") Long userId,@Param("status") Long status,@Param("startApplyTime") String startApplyTime,@Param("endApplyTime") String endApplyTime,@Param("startHandleTime") String startHandleTime,@Param("endHandleTime") String endHandleTime);
}
