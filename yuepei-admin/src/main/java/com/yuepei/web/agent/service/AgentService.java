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

    DeviceManageVo selectAgentInfo(Long userId,Long utilizationRate);

    DeviceWorkStatusVo selectDeviceDetailsByDeviceNumber(String deviceNumber);

    DeviceManageVo selectHospitalAdministration(Long userId, Long hospitalId, Long utilizationRate);

    String selectProportion(Long userId);

    String insertHospitalByAgent(HospitalAgentVo hospitalAgentVo,Long userId);

    List<UserLeaseOrderVo> selectLeaseOrder(Long userId,String deviceDepartment,String deviceTypeName,String nameOrNumber);

    AjaxResult insertAgentAccount(SysUser sysUser, Long userId);

//    List<FaultVo> selectDeviceFaultList(Long userId);
}
