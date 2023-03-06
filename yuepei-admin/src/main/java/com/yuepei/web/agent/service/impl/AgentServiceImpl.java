package com.yuepei.web.agent.service.impl;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.utils.SecurityUtils;
import com.yuepei.system.domain.*;
import com.yuepei.system.domain.vo.*;
import com.yuepei.system.mapper.*;
import com.yuepei.system.service.HospitalDeviceService;
import com.yuepei.web.agent.service.AgentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public List<DeviceDetailsVo> selectAgentInfo(Long userId) {
        Agent agent = agentMapper.selectAgentByAgentId(userId);
        List<Device> devices = new ArrayList<>();
        List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(agent.getAgentId());
        agentHospitals.stream().forEach(i->{
            List<Device> device = deviceMapper.selectDeviceByHospitalId(i.getHospitalId());
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
                    deviceDetailsVo.setDeviceFloor(split[0]);
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
        Agent agent = agentMapper.selectAgentByAgentId(userId);
        List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(agent.getAgentId());
        List<HospitalManagementVo> hospitalManagementVos = new ArrayList<>();
        agentHospitals.stream().forEach(i->{
            List<Device> device = deviceMapper.selectDeviceByHospitalId(i.getHospitalId());
            Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(i.getHospitalId());
            String deviceAddress = hospitalDeviceMapper.selectDeviceByDeviceAddress(i.getHospitalId());
            HospitalManagementVo hospitalManagementVo = new HospitalManagementVo();
            hospitalManagementVo.setAgentId(agent.getAgentId());
            hospitalManagementVo.setHospitalId(i.getHospitalId());
            hospitalManagementVo.setHospitalName(hospital.getHospitalName());
            hospitalManagementVo.setDeviceNum(device.size());
            hospitalManagementVo.setDeviceAddress(deviceAddress);
            hospitalManagementVos.add(hospitalManagementVo);
        });
        return hospitalManagementVos;
    }

    @Override
    public String insertHospitalByAgent(HospitalAgentVo hospitalAgentVo,String userName) {
        AgentHospital agentHospital = agentMapper.selectAgentHospital(hospitalAgentVo.getHospitalId());
        SysUser su = sysUserMapper.selectUserByUserName(userName);
        Agent agent = agentMapper.selectAgentByUserName(su.getUserName());
        if (su.getParentId()!=0){
            SysUser userById = sysUserMapper.selectUserById(su.getParentId());
            agent = agentMapper.selectAgentByUserName(userById.getUserName());
        }
        if (agentHospital!=null){
            if (agentHospital.getAgentId()==agent.getAgentId()){
                return "该医院已被您代理";
            }
            return "该医院已被其他代理商代理";
        }
        SysUser user = sysUserMapper.selectUserByUserName(hospitalAgentVo.getAccountNumber());
        if (user!=null){
            return "该账号已被使用,请重新输入账号";
        }
        //添加代理商和医院关联信息
        agentMapper.insertAgentHospital(agent.getAgentId(),hospitalAgentVo.getHospitalId());
        SysUser sysUser = new SysUser();
        //添加用户信息
        sysUser.setUserName(hospitalAgentVo.getAccountNumber());
        sysUser.setPassword(SecurityUtils.encryptPassword(hospitalAgentVo.getPassword()));
        sysUser.setNickName(hospitalAgentVo.getContacts());
        sysUserMapper.insertUser(sysUser);
        //添加用户和医院关联信息
        agentMapper.insertHospitalUser(hospitalAgentVo.getHospitalId(),hospitalAgentVo.getAccountNumber());
        //设备详细信息
        List<DeviceInfoVo> deviceDetails = hospitalAgentVo.getDeviceDetails();
        deviceDetails.stream().forEach(map->{
            Device device = new Device();
            device.setDeviceFullAddress(map.getDeviceFllor()+"," +
                    map.getDeviceDepartment()+"," +
                    map.getDeviceRoom()+"," +
                    map.getDeviceBed());
            device.setDeviceAddress(hospitalAgentVo.getHospitalAddress());
            device.setDeviceTypeId(map.getDeviceTypeId());
            device.setHospitalId(map.getHospitalId());
            device.setDeviceNumber(map.getDeviceNumber());
            device.setCreateTime(new Date());
            deviceMapper.insertDevice(device);
        });
        return "添加成功";
    }

    @Override
    public List<UserLeaseOrderVo> selectLeaseOrder(Long userId,String deviceDepartment,String deviceTypeName,String nameOrNumber) {
        Agent agent = agentMapper.selectAgentByAgentId(userId);
        List<UserLeaseOrderVo> userLeaseOrderList = new ArrayList<>();
        List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(agent.getAgentId());
        agentHospitals.stream().forEach(i->{
            SysUser user = sysUserMapper.selectUserById(userId);
            List<UserLeaseOrderVo> userLeaseOrders = hospitalDeviceService.selectLeaseOrder(user.getUserName(),deviceDepartment,deviceTypeName,nameOrNumber);
            userLeaseOrderList.addAll(userLeaseOrders);
        });
        return userLeaseOrderList;
    }

    @Override
    public AjaxResult insertAgentAccount(SysUser sysUser, Long userId) {
        SysUser user = sysUserMapper.selectUserByUserName(sysUser.getUserName());
        if (user.getUserName()==sysUser.getUserName()){
            return AjaxResult.error("该账号已被使用,请重新输入");
        }

        return null;
    }

    /*@Override
    public List<FaultVo> selectDeviceFaultList(Long userId) {
        List<Agent> agents = agentMapper.selectAgentByAgentId(userId);
        List<FaultVo> faultVos = new ArrayList<>();
        agents.stream().forEach(map->{
            List<Device> device = deviceMapper.selectDeviceByHospitalId(map.getHospitalId());
            List<Device> deviceList = device.stream().filter(i -> i.getStatus().equals("1")).collect(Collectors.toList());
            FaultVo faultVo = new FaultVo();

        });
        return null;
    }*/


}
