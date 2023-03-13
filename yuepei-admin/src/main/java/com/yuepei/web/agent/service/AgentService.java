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

    DeviceManageVo selectAgentInfo(Long userId,Long utilizationRate);

    DeviceWorkStatusVo selectDeviceDetailsByDeviceNumber(String deviceNumber);

    DeviceManageVo selectHospitalAdministration(Long userId, Long hospitalId, Long utilizationRate);

    String selectProportion(Long userId);

    String insertHospitalByAgent(HospitalAgentVo hospitalAgentVo,Long userId);

    List<UserLeaseOrderVo> selectLeaseOrder(Long userId,String deviceDepartment,String deviceTypeName,String nameOrNumber);

    Agent selectAgentByUser(Long userId);

    String insertAgentAccount(SubAccountVo subAccountVo, Long userId);

    List<SubAccountManageVo> selectSubAccount(Long userId);

    AgentDeviceVo selectAgentByDevice(Long userId);

    List<String> selectDeviceList();

    DeviceStatisticsVo selectDeviceTypeDetails(Long userId, Long deviceTypeId, Long hospitalId, String deviceDepartment, Long utilizationRate);

    TotalVo selectAgentRevenueStatistics(int statistics, Long userId);

//    List<FaultVo> selectDeviceFaultList(Long userId);
}
