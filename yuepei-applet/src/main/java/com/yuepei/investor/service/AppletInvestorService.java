package com.yuepei.investor.service;

import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.investor.domain.vo.InvestorDeviceManageVo;
import com.yuepei.investor.domain.vo.InvestorHospitalVO;
import com.yuepei.system.domain.DeviceType;
import com.yuepei.system.domain.vo.*;

import java.util.List;
import java.util.Map;

public interface AppletInvestorService {
    Map<String,Object> selectHome(SysUser user, InvestorHospitalVO investorHospitalVO);

    Map<String,Object> yesterdayRevenue(SysUser user, Long hospitalId);

    Map<String,Object> todayRevenue(SysUser user, Long hospitalId);

    Map<String,Object> monthRevenue(SysUser user, Long hospitalId);

    Map<String,Object> accumulativeRevenue(SysUser user, Long hospitalId);

    List<HospitalVO> selectHospitalName(Long userId);

    IndexVo indexPage(Long userId, Long hospitalId);

    TotalVo selectRevenueStatistics(Long userId, int statistics);

    List<List<String>> selectDepartment(Long userId);

    List<DeviceType> selectDeviceType(Long userId);

    List<HospitalVO> selectDeviceTypeByHospital(Long userId, Long deviceTypeId);

    InvestorDeviceManageVo investorDeviceManage(Long userId, Long hospitalId, String departmentName, Long utilizationRate);

    PersonalCenterVo investorPersonalCenter(Long userId);

    SysUser investorPersonalData(Long userId);

    Long selectProportion(Long userId);

    List<SubAccountManageVo> investorSubAccount(Long userId);

    int investorUploadsFile(FeedbackInfoVo feedbackInfoVo);

    List<FeedbackInfoVo> investorUploadsFileList(Long userId);

    FeedbackInfoVo investorUploadsFileListDetails(Long feedbackId);

    List<UserLeaseOrderVo> investorLeaseOrder(Long userId, String status, String deviceDepartment, String deviceTypeName, String nameOrNumber);

    UserLeaseOrderVo investorLeaseOrderDetails(String orderNumber, Long userId);
}
