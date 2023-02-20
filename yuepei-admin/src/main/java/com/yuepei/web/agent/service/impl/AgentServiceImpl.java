package com.yuepei.web.agent.service.impl;

import com.yuepei.system.domain.*;
import com.yuepei.system.domain.vo.*;
import com.yuepei.system.mapper.AgentMapper;
import com.yuepei.system.mapper.DeviceMapper;
import com.yuepei.system.mapper.DeviceTypeMapper;
import com.yuepei.system.mapper.HospitalDeviceMapper;
import com.yuepei.web.agent.service.AgentService;
import com.yuepei.web.hospital.service.HospitalDeviceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;

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

    @Autowired
    private HospitalDeviceService hospitalDeviceService;

    @Override
    public List<DeviceDetailsVo> selectAgentInfo(Long userId) {
        List<Agent> agent = agentMapper.selectAgentByAgentId(userId);
        List<Device> devices = new ArrayList<>();
        agent.stream().forEach(map->{
            List<Device> device = deviceMapper.selectDeviceByHospitalId(map.getHospitalId());
            devices.addAll(device);
        });
        List<DeviceDetailsVo> deviceDetailsVos = new ArrayList<>();
        devices.stream().forEach(map->{
            DeviceType deviceType = deviceTypeMapper.selectDeviceTypeByDeviceTypeId(map.getDeviceTypeId());
            DeviceDetailsVo deviceDetailsVo = new DeviceDetailsVo();
            String device_full_address = map.getDeviceFullAddress();
            if (!device_full_address.isEmpty()) {
                String[] split = device_full_address.split(",");
                for (int j = 0; j < split.length; j++) {
                    deviceDetailsVo.setDeviceFllor(split[0]);
                    deviceDetailsVo.setDeviceDepartment(split[1]);
                    deviceDetailsVo.setDeviceRoom(split[2]);
                    deviceDetailsVo.setDeviceBed(split[3]);
                }
            }
            deviceDetailsVo.setDeviceFullAddress(device_full_address);
            deviceDetailsVo.setDeviceNumber(map.getDeviceNumber());
            deviceDetailsVo.setStatus(map.getStatus());
            Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(map.getHospitalId());
            deviceDetailsVo.setHospitalId(hospital.getHospitalId());
            deviceDetailsVo.setHospitalName(hospital.getHospitalName());
            BeanUtils.copyProperties(deviceType,deviceDetailsVo);
            deviceDetailsVos.add(deviceDetailsVo);
        });
        return deviceDetailsVos;
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
            workStatusVo.setDeviceTypeId(deviceType.getDeviceTypeId());
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

    @Override
    public List<HospitalManagementVo> selectHospitalAdministration(Long userId) {
        List<Agent> agent = agentMapper.selectAgentByAgentId(userId);
        List<Device> devices = new ArrayList<>();
        agent.stream().forEach(map->{
            List<Device> device = deviceMapper.selectDeviceByHospitalId(map.getHospitalId());
            devices.addAll(device);
        });
        List<HospitalManagementVo> hospitalManagementVos = new ArrayList<>();
        devices.stream().forEach(map->{
            HospitalManagementVo hospitalManagementVo = new HospitalManagementVo();
            Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(map.getHospitalId());
            hospitalManagementVo.setHospitalId(hospital.getHospitalId());
            hospitalManagementVo.setHospitalName(hospital.getHospitalName());
            hospitalManagementVo.setDeviceAddress(map.getDeviceAddress());
            List<Device> deviceList = deviceMapper.selectDeviceByHospitalId(hospital.getHospitalId());
            hospitalManagementVo.setDeviceNum(deviceList.size());
            hospitalManagementVos.add(hospitalManagementVo);
        });
        return hospitalManagementVos;
    }


    /*需求修改*/
    @Override
    public String insertHospitalByAgent(HospitalAgentVo hospitalAgentVo) {
        Hospital hospital = hospitalDeviceMapper.selectHospital(hospitalAgentVo.getHospitalName());
        if (hospital!=null){
            return "该医院已被其他代理商代理";
        }
        hospitalDeviceMapper.insertHospital(hospitalAgentVo.getHospitalName());

        return null;
    }

    @Override
    public List<UserLeaseOrderVo> selectLeaseOrder(Long userId) {
        List<Agent> agents = agentMapper.selectAgentByAgentId(userId);
        List<UserLeaseOrderVo> userLeaseOrderList = new ArrayList<>();
        agents.stream().forEach(map->{
            List<UserLeaseOrderVo> userLeaseOrders = hospitalDeviceService.selectLeaseOrder(map.getHospitalId());
            userLeaseOrderList.addAll(userLeaseOrders);
        });
        return userLeaseOrderList;
    }


}
