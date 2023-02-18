package com.yuepei.web.agent.service.impl;

import com.yuepei.system.domain.Agent;
import com.yuepei.system.domain.Device;
import com.yuepei.system.domain.DeviceType;
import com.yuepei.system.domain.Hospital;
import com.yuepei.system.domain.vo.DeviceDetailsVo;
import com.yuepei.system.domain.vo.DeviceWorkStatusVo;
import com.yuepei.system.mapper.AgentMapper;
import com.yuepei.system.mapper.DeviceMapper;
import com.yuepei.system.mapper.DeviceTypeMapper;
import com.yuepei.system.mapper.HospitalDeviceMapper;
import com.yuepei.web.agent.service.AgentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zzy
 * @date 2023/2/17 15:05
 */
@Service
public class AgentServiceImpl implements AgentService {

    @Autowired
    private AgentMapper agentMapper;

    @Autowired
    private HospitalDeviceMapper hospitalDeviceMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    @Override
    public List<DeviceDetailsVo> selectAgentInfo(Long userId) {
        Agent agent = agentMapper.selectAgentByAgentId(userId);
        /*Device device = deviceMapper.selectDeviceByDeviceNumber(agent.getDeviceNumber());
        Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(device.getHospitalId());
        DeviceType deviceType = deviceTypeMapper.selectDeviceTypeByDeviceTypeId(device.getDeviceTypeId());
        List<DeviceDetailsVo> deviceDetailsVos = new ArrayList<>();
        DeviceDetailsVo deviceDetailsVo = new DeviceDetailsVo();
        String device_full_address = device.getDeviceFullAddress();
        if (!device_full_address.isEmpty()) {
            String[] split = device_full_address.split(",");
            for (int j = 0; j < split.length; j++) {
                deviceDetailsVo.setDeviceFllor(split[0]);
                deviceDetailsVo.setDeviceDepartment(split[1]);
                deviceDetailsVo.setDeviceRoom(split[2]);
                deviceDetailsVo.setDeviceBed(split[3]);
            }
            deviceDetailsVo.setDeviceFullAddress(device_full_address);
            deviceDetailsVo.setDeviceNumber(device.getDeviceNumber());
            deviceDetailsVo.setStatus(device.getStatus());
            BeanUtils.copyProperties(hospital,deviceDetailsVo);
            BeanUtils.copyProperties(deviceType,deviceDetailsVo);
            deviceDetailsVos.add(deviceDetailsVo);
        }*/
        return null;
    }

    @Override
    public DeviceWorkStatusVo selectDeviceDetailsByDeviceNumber(String deviceNumber) {
        Device device = deviceMapper.selectDeviceByDeviceNumber(deviceNumber);
        Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(device.getHospitalId());
        DeviceType deviceType = deviceTypeMapper.selectDeviceTypeByDeviceTypeId(device.getDeviceTypeId());
        DeviceWorkStatusVo workStatusVo = new DeviceWorkStatusVo();
        String device_full_address = device.getDeviceFullAddress();
        if (!device_full_address.isEmpty()) {
            String[] split = device_full_address.split(",");
            StringBuilder builder = new StringBuilder();
            builder.append(hospital.getHospitalName()).append(split[0]).append(split[1]).append(split[2]).append(split[3]);
            workStatusVo.setDeviceTypeName(deviceType.getDeviceTypeName());
            workStatusVo.setDeviceNumber(device.getDeviceNumber());
            workStatusVo.setDeviceAddress(device.getDeviceAddress());
            workStatusVo.setDeviceFullAddress(builder.toString());
            workStatusVo.setStatus(null);
            workStatusVo.setPower(0);
            workStatusVo.setHeartbeat(null);
            workStatusVo.setCumulativeTime(null);
        }
        return workStatusVo;
    }


}
