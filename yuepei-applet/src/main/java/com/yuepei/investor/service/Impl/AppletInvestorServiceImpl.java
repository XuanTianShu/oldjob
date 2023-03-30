package com.yuepei.investor.service.Impl;

import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.investor.domain.vo.InvestorHospitalVO;
import com.yuepei.investor.domain.vo.InvestorOrderVO;
import com.yuepei.investor.domain.vo.InvestorRevenueVO;
import com.yuepei.investor.mapper.AppletInvestorMapper;
import com.yuepei.investor.service.AppletInvestorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppletInvestorServiceImpl implements AppletInvestorService {

    @Autowired
    private AppletInvestorMapper appletInvestorMapper;

    /**
     * 首页
     * @param user
     * @param investorHospitalVO
     * @return
     */
    @Override
    public Map<String, Object> selectHome(SysUser user, InvestorHospitalVO investorHospitalVO) {
        Map<String, Object> map = new HashMap<>();
        //医院
        List<InvestorHospitalVO> hospitalList = appletInvestorMapper.selectHospitalByInvestorId(user.getUserId());
        //设备数量
        int deviceCount = appletInvestorMapper.selectDeviceCountByInvestorId(user.getUserId(),investorHospitalVO.getHospitalId());
        //设备总收益
        BigDecimal deviceEarning = appletInvestorMapper.selectDeviceEarningsByInvestorId(user.getUserId(),investorHospitalVO.getHospitalId());
        map.put("hospitalList",hospitalList);
        map.put("deviceCount",deviceCount);
        map.put("deviceEarning",deviceEarning);
        return map;
    }

    /**
     * 昨日营收
     * @param user
     * @param hospitalId
     * @return
     */
    @Override
    public Map<String, Object> yesterdayRevenue(SysUser user, Long hospitalId) {
        Map<String, Object> map = new HashMap<>();
        InvestorRevenueVO investorRevenueVO = appletInvestorMapper.yesterdayRevenue(user.getUserId(),hospitalId);
        List<InvestorOrderVO> investorOrderVOList = appletInvestorMapper.yesterdayRevenueList(user.getUserId(),hospitalId);
        investorRevenueVO.setInvestorOrderVOList(investorOrderVOList);
        map.put("revenueVO", investorRevenueVO);
        return map;
    }

    /**
     * 今日营收
     * @param user
     * @param hospitalId
     * @return
     */
    @Override
    public Map<String, Object> todayRevenue(SysUser user, Long hospitalId) {
        Map<String, Object> map = new HashMap<>();
        InvestorRevenueVO investorRevenueVO = appletInvestorMapper.todayRevenue(user.getUserId(),hospitalId);
        List<InvestorOrderVO> investorOrderVOList = appletInvestorMapper.todayRevenueList(user.getUserId(),hospitalId);
        investorRevenueVO.setInvestorOrderVOList(investorOrderVOList);
        map.put("revenueVO", investorRevenueVO);
        return map;
    }

    /**
     * 本月营收
     * @param user
     * @param hospitalId
     * @return
     */
    @Override
    public Map<String, Object> monthRevenue(SysUser user, Long hospitalId) {
        Map<String, Object> map = new HashMap<>();
        InvestorRevenueVO investorRevenueVO = appletInvestorMapper.monthRevenue(user.getUserId(),hospitalId);
        List<InvestorOrderVO> investorOrderVOList = appletInvestorMapper.monthRevenueList(user.getUserId(),hospitalId);
        investorRevenueVO.setInvestorOrderVOList(investorOrderVOList);
        map.put("revenueVO", investorRevenueVO);
        return map;
    }

    /**
     * 累计营收
     * @param user
     * @param hospitalId
     * @return
     */
    @Override
    public Map<String, Object> accumulativeRevenue(SysUser user, Long hospitalId) {
        Map<String, Object> map = new HashMap<>();
        InvestorRevenueVO investorRevenueVO = appletInvestorMapper.accumulativeRevenue(user.getUserId(),hospitalId);
        List<InvestorOrderVO> investorOrderVOList = appletInvestorMapper.accumulativeRevenueList(user.getUserId(),hospitalId);
        investorRevenueVO.setInvestorOrderVOList(investorOrderVOList);
        map.put("revenueVO", investorRevenueVO);
        return map;
    }
}
