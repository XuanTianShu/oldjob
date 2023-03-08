package com.yuepei.web.agent.service.impl;

import com.alibaba.fastjson2.JSON;
import com.google.gson.Gson;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.utils.SecurityUtils;
import com.yuepei.common.utils.uuid.UUID;
import com.yuepei.system.domain.*;
import com.yuepei.system.domain.vo.*;
import com.yuepei.system.mapper.*;
import com.yuepei.system.service.HospitalDeviceService;
import com.yuepei.web.agent.service.AgentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private UserLeaseOrderMapper userLeaseOrderMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public DeviceManageVo selectAgentInfo(Long userId,Long utilizationRate) {
        DeviceManageVo manageVo = new DeviceManageVo();
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
                String[] array = JSON.parseArray(device_full_address).toArray(new String[0]);
                Hospital deviceFloor = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[0]));
                Hospital Department = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[1]));
                Hospital deviceRoom = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[2]));
                Hospital deviceBed = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[3]));
                deviceDetailsVo.setDeviceFloor(deviceFloor.getHospitalId());
                deviceDetailsVo.setDeviceDepartment(Department.getHospitalId());
                deviceDetailsVo.setDeviceRoom(deviceRoom.getHospitalId());
                deviceDetailsVo.setDeviceBed(deviceBed.getHospitalId());
            }
            deviceDetailsVo.setDeviceFullAddress(device_full_address);
            deviceDetailsVo.setDeviceNumber(map.getDeviceNumber());
            deviceDetailsVo.setStatus(map.getStatus());
            Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(map.getHospitalId());
            BeanUtils.copyProperties(hospital,deviceDetailsVo);
            BeanUtils.copyProperties(deviceType,deviceDetailsVo);
            deviceDetailsVos.add(deviceDetailsVo);
        });
        BigDecimal decimal = new BigDecimal(0);
        List<UserLeaseOrder> userLeaseOrders = new ArrayList<>();
        deviceDetailsVos.stream().forEach(map->{
            List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectUserLeaseOrderByDeviceNumber(map.getDeviceNumber());
            userLeaseOrders.addAll(userLeaseOrderList);
        });
        userLeaseOrders.stream().forEach(map->{
            BigDecimal bigDecimal = new BigDecimal(map.getNetAmount());
            decimal.add(bigDecimal);
        });
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        manageVo.setDeviceAmount(decimal.multiply(new BigDecimal(sysUser.getProportion())).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
        manageVo.setUtilizationRate(null);
        manageVo.setDetailsVos(deviceDetailsVos);
        return manageVo;
    }

    @Override
    public DeviceWorkStatusVo selectDeviceDetailsByDeviceNumber(String deviceNumber) {
        Device device = deviceMapper.selectDeviceByDeviceNumber(deviceNumber);
        DeviceType deviceType = deviceTypeMapper.selectDeviceTypeByDeviceTypeId(device.getDeviceTypeId());
        DeviceWorkStatusVo workStatusVo = new DeviceWorkStatusVo();
        String device_full_address = device.getDeviceFullAddress();
        if (!device_full_address.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            String[] array = JSON.parseArray(device_full_address).toArray(new String[0]);
            Hospital deviceFloor = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[0]));
            Hospital Department = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[1]));
            Hospital deviceRoom = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[2]));
            Hospital deviceBed = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[3]));
            builder.append(deviceFloor.getHospitalName()).append(Department.getHospitalName()).append(deviceRoom.getHospitalName()).append(deviceBed.getHospitalName());
            workStatusVo.setDeviceTypeId(deviceType.getDeviceTypeId());
            workStatusVo.setDeviceTypeName(deviceType.getDeviceTypeName());
            workStatusVo.setDeviceNumber(device.getDeviceNumber());
            workStatusVo.setDeviceAddress(device.getDeviceAddress());
            workStatusVo.setDeviceFullAddress(String.valueOf(builder));
            workStatusVo.setStatus(device.getStatus());
            workStatusVo.setPower(0);
            workStatusVo.setHeartbeat(null);
            workStatusVo.setCumulativeTime(null);
        }
        return workStatusVo;
    }

    @Override
    public DeviceManageVo selectHospitalAdministration(Long userId, Long hospitalId, Long utilizationRate) {
        DeviceManageVo deviceManageVo = new DeviceManageVo();
        Agent agent = agentMapper.selectAgentByAgentId(userId);
        List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(agent.getAgentId());
        List<HospitalManagementVo> hospitalManagementVos = new ArrayList<>();
        List<Device> deviceList = new ArrayList<>();
        agentHospitals.stream().forEach(i->{
            List<Device> device = deviceMapper.selectDeviceByHospitalId(i.getHospitalId());
            deviceList.addAll(device);
        });
        deviceList.stream().forEach(map->{
            Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(map.getHospitalId());
            List<HospitalUser> hospitalUser = hospitalDeviceMapper.selectHospitalByHospitalUserName(map.getHospitalId());
            hospitalUser.stream().forEach(i->{
                SysUser sysUser = sysUserMapper.selectUserByHospital(i.getUserName());
                if (sysUser!=null){
                    String device_full_address = map.getDeviceFullAddress();
                    StringBuilder builder = new StringBuilder();
                    String[] array = JSON.parseArray(device_full_address).toArray(new String[0]);
                    Hospital deviceFloor = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[0]));
                    Hospital Department = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[1]));
                    Hospital deviceRoom = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[2]));
                    Hospital deviceBed = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[3]));
                    builder.append(deviceFloor.getHospitalName()).append(Department.getHospitalName()).append(deviceRoom.getHospitalName()).append(deviceBed.getHospitalName());
                    HospitalManagementVo hospitalManagementVo = new HospitalManagementVo();
                    hospitalManagementVo.setHospitalId(hospital.getHospitalId());
                    hospitalManagementVo.setHospitalName(hospital.getHospitalName());
                    hospitalManagementVo.setDeviceNum(deviceList.size());
                    hospitalManagementVo.setDeviceAddress(String.valueOf(builder));
                    hospitalManagementVo.setProportion(sysUser.getProportion());
                    hospitalManagementVos.add(hospitalManagementVo);
                }
            });
        });
        if (hospitalId!=null){
            List<HospitalManagementVo> collect = hospitalManagementVos.stream().filter(map -> map.getHospitalId().equals(hospitalId)).collect(Collectors.toList());
            hospitalManagementVos.clear();
            hospitalManagementVos.addAll(collect);
        }
        List<Device> devices = new ArrayList<>();
        hospitalManagementVos.stream().forEach(map->{
            List<Device> deviceNumber = hospitalDeviceMapper.selectDeviceByHospitalId(map.getHospitalId());
            devices.addAll(deviceNumber);
        });
        BigDecimal decimal = new BigDecimal(0);
        devices.stream().forEach(map->{
            List<UserLeaseOrderVo> userLeaseOrderVos = new ArrayList<>();
            List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectUserLeaseOrderByDeviceNumber(map.getDeviceNumber());
            userLeaseOrderList.stream().forEach(i->{
                UserLeaseOrderVo userLeaseOrderVo = new UserLeaseOrderVo();
                BeanUtils.copyProperties(i,userLeaseOrderVo);
                userLeaseOrderVo.setNetAmount(new BigDecimal(i.getNetAmount()));
            });
            BigDecimal reduce = userLeaseOrderVos.stream().map(UserLeaseOrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            decimal.add(reduce);
        });
        deviceManageVo.setDeviceAmount(decimal);
        deviceManageVo.setUtilizationRate(null);
        deviceManageVo.setHospitalManagementVos(hospitalManagementVos);
        return deviceManageVo;
    }

    @Override
    public String selectProportion(Long userId) {
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        if (!String.valueOf(sysUser.getParentId()).equals("0")){
            SysUser userById = sysUserMapper.selectUserById(sysUser.getParentId());
            HospitalUser hospitalUser = hospitalDeviceMapper.selectHospitalbyUserName(userById.getUserName());
            List<HospitalUser> hospitalUsers = hospitalDeviceMapper.selectHospitalByHospitalUserName(hospitalUser.getHospitalId());
            hospitalUsers.stream().forEach(map->{
                SysUser user = sysUserMapper.selectUserByHospital(map.getUserName());
                if (user!=null){
                    userById.setProportion(userById.getProportion()-user.getProportion());
                }
            });
            return "可分配的分成比例为"+userById.getProportion()+"%";
        }else {
            HospitalUser hospitalUser = hospitalDeviceMapper.selectHospitalbyUserName(sysUser.getUserName());
            List<HospitalUser> hospitalUsers = hospitalDeviceMapper.selectHospitalByHospitalUserName(hospitalUser.getHospitalId());
            hospitalUsers.stream().forEach(map->{
                SysUser user = sysUserMapper.selectUserByHospital(map.getUserName());
                if (user!=null){
                    sysUser.setProportion(sysUser.getProportion()-user.getProportion());
                }
            });
            return "可分配的分成比例为"+sysUser.getProportion()+"%";
        }
    }

    @Override
    public String insertHospitalByAgent(HospitalAgentVo hospitalAgentVo,Long userId) {
        AgentHospital agentHospital = agentMapper.selectAgentHospital(hospitalAgentVo.getHospitalId());
        SysUser su = sysUserMapper.selectUserById(userId);
        Agent agent = agentMapper.selectAgentByUserName(su.getUserName());
        if (String.valueOf(su.getParentId()).equals("0")){
            SysUser userById = sysUserMapper.selectUserById(su.getParentId());
            agent = agentMapper.selectAgentByUserName(userById.getUserName());
            HospitalUser hospitalUser = hospitalDeviceMapper.selectHospitalbyUserName(userById.getUserName());
            List<HospitalUser> hospitalUsers = hospitalDeviceMapper.selectHospitalByHospitalUserName(hospitalUser.getHospitalId());
            List<SysUser> sysUsers = new ArrayList<>();
            hospitalUsers.stream().forEach(map->{
                SysUser sysUser = sysUserMapper.selectUserByHospital(map.getUserName());
                sysUsers.add(sysUser);
            });
            if (sysUsers.size()==2){
                return "您只能添加两个子代理";
            }
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
        sysUser.setNickName("医院用户");
        sysUser.setUserType("04");
        sysUser.setProportion(hospitalAgentVo.getDivided());
        sysUserMapper.insertUser(sysUser);
        //添加用户和医院关联信息
        agentMapper.insertHospitalUser(hospitalAgentVo.getHospitalId(),hospitalAgentVo.getAccountNumber());
        //设备详细信息
        List<DeviceInfoVo> deviceDetails = hospitalAgentVo.getDeviceDetails();
        deviceDetails.stream().forEach(map->{
            Device device = new Device();
            List<Long> fullAddress = new ArrayList<>();
            fullAddress.add(map.getDeviceFloor());
            fullAddress.add(map.getDeviceDepartment());
            fullAddress.add(map.getDeviceRoom());
            fullAddress.add(map.getDeviceBed());
            Gson gson = new Gson();
            String deviceFullAddress = gson.toJson(fullAddress);
            device.setDeviceFullAddress(deviceFullAddress);
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
            List<UserLeaseOrderVo> userLeaseOrders = hospitalDeviceService.selectLeaseOrder(user.getUserId(),deviceDepartment,deviceTypeName,nameOrNumber);
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
