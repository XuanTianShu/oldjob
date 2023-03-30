package com.yuepei.investor.service;

import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.investor.domain.vo.InvestorHospitalVO;

import java.util.Map;

public interface AppletInvestorService {
    Map<String,Object> selectHome(SysUser user, InvestorHospitalVO investorHospitalVO);

    Map<String,Object> yesterdayRevenue(SysUser user, Long hospitalId);

    Map<String,Object> todayRevenue(SysUser user, Long hospitalId);

    Map<String,Object> monthRevenue(SysUser user, Long hospitalId);

    Map<String,Object> accumulativeRevenue(SysUser user, Long hospitalId);
}
