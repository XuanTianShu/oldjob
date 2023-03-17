package com.yuepei.web.agent.service;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.system.domain.Agent;
import com.yuepei.system.domain.Device;
import com.yuepei.system.domain.DeviceType;
import com.yuepei.system.domain.Hospital;
import com.yuepei.system.domain.vo.*;

import java.util.List;

/**
 * @author zzy
 * @date 2023/2/17 15:05
 */
public interface AgentService {

    DeviceManageVo selectAgentInfo(Long userId,Long hospitalId,Long utilizationRate);

    DeviceWorkStatusVo selectDeviceDetailsByDeviceNumber(String deviceNumber);

    DeviceManageVo selectHospitalAdministration(Long userId, Long hospitalId, Long utilizationRate);

    String selectProportion(Long userId);

    String insertHospitalByAgent(HospitalAgentVo hospitalAgentVo);

    List<UserLeaseOrderVo> selectLeaseOrder(Long userId,String deviceDepartment,String deviceTypeName,String nameOrNumber);

    Agent selectAgentByUser(Long userId);

    String insertAgentAccount(SubAccountVo subAccountVo);

    List<SubAccountManageVo> selectSubAccount(Long userId);

    AgentDeviceVo selectAgentByDevice(Long userId);

    List<DeviceType> selectDeviceList(Long userId);

    DeviceStatisticsVo selectDeviceTypeDetails(Long userId, Long deviceTypeId, Long hospitalId, String deviceDepartment, Long utilizationRate);

    TotalVo selectAgentRevenueStatistics(int statistics, Long userId);

    List<String> selectDepartment(Long userId);

    List<Hospital> selectHospitalList(Long userId);

    List<FeedbackInfoVo> selectDeviceFaultList(Long userId,Integer status,String numberOrAddress);

    List<FeedbackInfoVo> selectDeviceFaultDetails(Long userId, Integer status, Long feedbackId);
}
