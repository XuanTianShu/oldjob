package com.yuepei.investor.service;

import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.investor.domain.vo.InvestorHospitalVO;
import com.yuepei.system.domain.DeviceType;
import com.yuepei.system.domain.vo.IndexVo;
import com.yuepei.system.domain.vo.TotalVo;

import java.util.List;
import java.util.Map;

public interface AppletInvestorService {
    Map<String,Object> selectHome(SysUser user, InvestorHospitalVO investorHospitalVO);

    Map<String,Object> yesterdayRevenue(SysUser user, Long hospitalId);

    Map<String,Object> todayRevenue(SysUser user, Long hospitalId);

    Map<String,Object> monthRevenue(SysUser user, Long hospitalId);

    Map<String,Object> accumulativeRevenue(SysUser user, Long hospitalId);

    IndexVo indexPage(Long userId);

    TotalVo selectRevenueStatistics(Long userId, int statistics);

    List<DeviceType> selectDeviceType(Long userId);
}
