package com.yuepei.investor.mapper;

import com.yuepei.investor.domain.vo.InvestorHospitalVO;
import com.yuepei.investor.domain.vo.InvestorOrderVO;
import com.yuepei.investor.domain.vo.InvestorRevenueVO;
import com.yuepei.system.domain.OrderProportionDetail;
import com.yuepei.system.domain.UserLeaseOrder;
import com.yuepei.system.domain.vo.OrderProportionDetailVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface AppletInvestorMapper {

    /**
     * 医院
     * @param userId
     * @return
     */
    List<InvestorHospitalVO> selectHospitalByInvestorId(@Param("userId") Long userId);

    /**
     * 设备数量
     * @param userId
     * @param hospitalId
     * @return
     */
    int selectDeviceCountByInvestorId(@Param("userId") Long userId, @Param("hospitalId") Long hospitalId);

    /**
     * 设备总收益
     * @param userId
     * @param hospitalId
     * @return
     */
    BigDecimal selectDeviceEarningsByInvestorId(@Param("userId") Long userId, @Param("hospitalId") Long hospitalId);

    /**
     * 昨日营收
     * @param userId
     * @param hospitalId
     * @return
     */
    InvestorRevenueVO yesterdayRevenue(@Param("userId") Long userId, @Param("hospitalId") Long hospitalId);

    /**
     * 昨日营收详情
     * @param userId
     * @param hospitalId
     * @return
     */
    List<InvestorOrderVO> yesterdayRevenueList(@Param("userId") Long userId, @Param("hospitalId") Long hospitalId);

    /**
     * 今日营收
     * @param userId
     * @param hospitalId
     * @return
     */
    InvestorRevenueVO todayRevenue(@Param("userId") Long userId, @Param("hospitalId") Long hospitalId);

    /**
     * 今日营收详情
     * @param userId
     * @param hospitalId
     * @return
     */
    List<InvestorOrderVO> todayRevenueList(@Param("userId") Long userId, @Param("hospitalId") Long hospitalId);

    /**
     * 本月营收
     * @param userId
     * @param hospitalId
     * @return
     */
    InvestorRevenueVO monthRevenue(@Param("userId") Long userId, @Param("hospitalId") Long hospitalId);

    /**
     * 本月营收详情
     * @param userId
     * @param hospitalId
     * @return
     */
    List<InvestorOrderVO> monthRevenueList(@Param("userId") Long userId, @Param("hospitalId") Long hospitalId);

    /**
     * 累计营收
     * @param userId
     * @param hospitalId
     * @return
     */
    InvestorRevenueVO accumulativeRevenue(@Param("userId") Long userId, @Param("hospitalId") Long hospitalId);

    /**
     * 累计营收详情
     * @param userId
     * @param hospitalId
     * @return
     */
    List<InvestorOrderVO> accumulativeRevenueList(@Param("userId") Long userId, @Param("hospitalId") Long hospitalId);

    List<UserLeaseOrder> selectUserLeaseOrderByDevices(@Param("deviceNumber") List<String> deviceNumbers,@Param("investorId") String investorId);

    List<OrderProportionDetailVo> selectOrderProtionDetail(@Param("userId") Long userId);

    List<UserLeaseOrder> selectUserLeaseOrderByOrderNumber(@Param("nameOrNumber") String nameOrNumber,@Param("investorId") String investorId);
}
