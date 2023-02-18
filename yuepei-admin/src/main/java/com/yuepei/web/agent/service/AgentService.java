package com.yuepei.web.agent.service;

import com.yuepei.system.domain.vo.DeviceDetailsVo;
import com.yuepei.system.domain.vo.DeviceWorkStatusVo;

import java.util.List;

/**
 * @author zzy
 * @date 2023/2/17 15:05
 */
public interface AgentService {

    List<DeviceDetailsVo> selectAgentInfo(Long userId);

    DeviceWorkStatusVo selectDeviceDetailsByDeviceNumber(String deviceNumber);

//    String selectHospitalAdministration(Long userId);
}
