package com.yuepei.web.agent.service;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.system.domain.vo.*;

import java.util.List;

/**
 * @author zzy
 * @date 2023/2/17 15:05
 */
public interface AgentService {

    List<DeviceDetailsVo> selectAgentInfo(Long userId);

    DeviceWorkStatusVo selectDeviceDetailsByDeviceNumber(String deviceNumber);

    List<HospitalManagementVo> selectHospitalAdministration(Long userId);

    String insertHospitalByAgent(HospitalAgentVo hospitalAgentVo,String userName);

    List<UserLeaseOrderVo> selectLeaseOrder(Long userId);

    AjaxResult insertAgentAccount(SysUser sysUser, Long userId);

//    List<FaultVo> selectDeviceFaultList(Long userId);
}
