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
import java.util.Arrays;
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
        List<HospitalManagementVo> managementVos = hospitalManagementVos.stream().distinct().collect(Collectors.toList());
        List<Device> devices = new ArrayList<>();
        managementVos.stream().forEach(map->{
            List<Device> deviceNumber = hospitalDeviceMapper.selectDeviceByHospitalId(map.getHospitalId());
            devices.addAll(deviceNumber);
        });
        List<Device> list = devices.stream().distinct().collect(Collectors.toList());
        BigDecimal decimal = new BigDecimal(0);
        list.stream().forEach(map->{
            List<UserLeaseOrderVo> userLeaseOrderVos = new ArrayList<>();
            List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectUserLeaseOrderByDeviceNumber(map.getDeviceNumber());
            userLeaseOrderList.stream().forEach(i->{
                BigDecimal bigDecimal = new BigDecimal(i.getNetAmount());
                decimal.add(bigDecimal);
            });
        });
        deviceManageVo.setDeviceAmount(decimal);
        if (hospitalId!=null){
            List<HospitalManagementVo> collect = managementVos.stream().filter(map -> map.getHospitalId().equals(hospitalId)).collect(Collectors.toList());
            managementVos.clear();
            managementVos.addAll(collect);
            BigDecimal decimal1 = new BigDecimal(0);
            list.stream().forEach(map->{
                List<UserLeaseOrderVo> userLeaseOrderVos = new ArrayList<>();
                List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectUserLeaseOrderByDeviceNumber(map.getDeviceNumber());
                userLeaseOrderList.stream().forEach(i->{
                    BigDecimal bigDecimal = new BigDecimal(i.getNetAmount());
                    decimal1.add(bigDecimal);
                });
            });

            deviceManageVo.setDeviceAmount(decimal);
        }
        deviceManageVo.setHospitalSum(agentHospitals.size());
        deviceManageVo.setDeviceSum(list.size());
        deviceManageVo.setUtilizationRate(null);
        deviceManageVo.setHospitalManagementVos(managementVos);
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
        String deviceNumber = hospitalAgentVo.getDeviceNumber();
        String[] device = deviceNumber.split(",");
        for (String s : device) {
            Device devices = deviceMapper.selectDeviceByDeviceNumber(s);
            if (devices.getHospitalId()!=null){
                return  "设备编号为:"+s+"已被使用,请重新输入";
            }
            devices.setHospitalId(hospitalAgentVo.getHospitalId());
            deviceMapper.updateDevice(devices);
        }
        return "添加成功";
    }

    @Override
    public List<UserLeaseOrderVo> selectLeaseOrder(Long userId,String deviceDepartment,String deviceTypeName,String nameOrNumber) {
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        List<UserLeaseOrderVo> userLeaseOrderVoList = new ArrayList<>();
        if (sysUser.getParentId()!=0){
            List<UserLeaseOrderVo> userLeaseOrders = hospitalDeviceService.selectLeaseOrder(userId,deviceDepartment,deviceTypeName,nameOrNumber);
            userLeaseOrderVoList.addAll(userLeaseOrders);
        }else {
            List<SysUser> sysUsers = sysUserMapper.selectUserByParentId(userId);
            sysUsers.add(sysUser);
            sysUsers.stream().forEach(map->{
                List<UserLeaseOrderVo> userLeaseOrders = hospitalDeviceService.selectLeaseOrder(map.getUserId(),deviceDepartment,deviceTypeName,nameOrNumber);
                userLeaseOrderVoList.addAll(userLeaseOrders);
            });
        }
        return userLeaseOrderVoList;
    }

    @Override
    public Agent selectAgentByUser(Long userId){
        SysUser user = sysUserMapper.selectUserById(userId);
        Agent agent = agentMapper.selectAgentByUserName(user.getUserName());
        return agent;
    }

    @Override
    public String insertAgentAccount(SubAccountVo subAccountVo, Long userId) {
        List<SysUser> user = sysUserMapper.selectUserByParentId(userId);
        if (user.size()>=2){
            return "您只能添加两个子账户";
        }
        SysUser SysUser = sysUserMapper.selectUserById(userId);
        Agent agent = agentMapper.selectAgentByUserName(SysUser.getUserName());
        SysUser sysUser = new SysUser();
        sysUser.setUserName(subAccountVo.getUserName());
        sysUser.setPassword(SecurityUtils.encryptPassword(subAccountVo.getPassword()));
        sysUser.setPhoneNumber(subAccountVo.getNumber());
        sysUser.setRole(subAccountVo.getRole());
        sysUser.setProportion(subAccountVo.getProportion());
        sysUser.setArea(subAccountVo.getArea());
        sysUser.setAddressDetailed(subAccountVo.getAddress());
        sysUser.setContacts(subAccountVo.getContacts());
        sysUser.setUserType("05");
        sysUser.setParentId(userId);
        sysUser.setNickName("代理商用户");
        sysUserMapper.insertUser(sysUser);
        Agent agent1 = new Agent();
        agent1.setAgentName(agent.getAgentName());
        agent1.setUserName(subAccountVo.getUserName());
        agent1.setCreateTime(new Date());
        agent1.setUpdateTime(new Date());
        agentMapper.insertAgent(agent1);
        return "添加成功";
    }

    @Override
    public List<SubAccountManageVo> selectSubAccount(Long userId) {
        List<SysUser> sysUsers = sysUserMapper.selectUserByParentId(userId);
        List<SubAccountManageVo> subAccountManageVoList = new ArrayList<>();
        sysUsers.stream().forEach(map->{
            SubAccountManageVo subAccountManageVo = new SubAccountManageVo();
            Agent agent = agentMapper.selectAgentByUserName(map.getUserName());
            List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(agent.getAgentId());
            subAccountManageVo.setProportion(map.getProportion());
            subAccountManageVo.setAgentName(agent.getAgentName());
            subAccountManageVo.setHospitalSum(agentHospitals.size());
            if (agentHospitals.size()==0){
                subAccountManageVo.setDeviceSum(0);
            }
            agentHospitals.stream().forEach(i->{
                List<Device> deviceList = deviceMapper.selectDeviceByHospitalId(i.getHospitalId());
                subAccountManageVo.setDeviceSum(deviceList.size());
            });
            subAccountManageVoList.add(subAccountManageVo);
        });
        return subAccountManageVoList;
    }

    @Override
    public AgentDeviceVo selectAgentByDevice(Long userId) {
        AgentDeviceVo agentDeviceVo = new AgentDeviceVo();
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        if (sysUser.getParentId()!=0){
            //该账号为子代理
            List<DeviceType> deviceType = hospitalDeviceMapper.selectDeviceType(sysUser.getUserId());
            Agent agent = agentMapper.selectAgentByUserName(sysUser.getUserName());
            List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(agent.getAgentId());
            List<Device> deviceList = new ArrayList<>();
            List<UserLeaseOrder> userLeaseOrderList = new ArrayList<>();
            agentDeviceVo.setHospitalSum(agentHospitals.size());
            if (agentHospitals.size()!=0){
                agentHospitals.stream().forEach(i->{
                    List<Device> devices = deviceMapper.selectDeviceByHospitalId(i.getHospitalId());
                    deviceList.addAll(devices);
                    deviceList.stream().forEach(map->{
                        UserLeaseOrder userLeaseOrder = userLeaseOrderMapper.selectUseDevice(map.getDeviceNumber());
                        userLeaseOrderList.add(userLeaseOrder);
                    });
                });
            }
            agentDeviceVo.setHospitalSum(agentHospitals.size());
            agentDeviceVo.setDeviceSum(deviceList.size());
            agentDeviceVo.setUseDeviceSum(userLeaseOrderList.size());
            agentDeviceVo.setDeviceTypes(deviceType);
            return agentDeviceVo;
        }else {
            //该账号为主代理
            List<SysUser> sysUsers = sysUserMapper.selectUserByParentId(userId);
            sysUsers.add(sysUser);
            List<DeviceType> deviceTypes = new ArrayList<>();
            List<Device> deviceList = new ArrayList<>();
            List<AgentHospital> agentHospitalList = new ArrayList<>();
            List<UserLeaseOrder> userLeaseOrderList = new ArrayList<>();
            sysUsers.stream().forEach(map->{
                List<DeviceType> deviceType = hospitalDeviceMapper.selectDeviceType(map.getUserId());
                deviceTypes.addAll(deviceType);
                Agent agent = agentMapper.selectAgentByUserName(map.getUserName());
                List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(agent.getAgentId());
                agentHospitalList.addAll(agentHospitals);
                if (agentHospitals.size()!=0){
                    agentHospitals.stream().forEach(i->{
                        List<Device> devices = deviceMapper.selectDeviceByHospitalId(i.getHospitalId());
                        deviceList.addAll(devices);
                        deviceList.stream().forEach(j->{
                            UserLeaseOrder userLeaseOrder = userLeaseOrderMapper.selectUseDevice(j.getDeviceNumber());
                            if (userLeaseOrder!=null){
                                userLeaseOrderList.add(userLeaseOrder);
                            }
                        });
                    });
                }
            });
            List<UserLeaseOrder> collect = userLeaseOrderList.stream().distinct().collect(Collectors.toList());
            agentDeviceVo.setHospitalSum(agentHospitalList.size());
            agentDeviceVo.setDeviceSum(deviceList.size());
            agentDeviceVo.setUseDeviceSum(collect.size());
            agentDeviceVo.setDeviceTypes(deviceTypes);
            return agentDeviceVo;
        }
    }

    @Override
    public List<String> selectDeviceList() {
        List<String> deviceNumber = new ArrayList<>();
        List<Device> devices = deviceMapper.selectDeviceAll();
        for (Device device : devices) {
            if (device.getHospitalId()==null){
                deviceNumber.add(device.getDeviceNumber());
            }
        }
        return deviceNumber;
    }

    @Override
    public DeviceStatisticsVo selectDeviceTypeDetails(Long userId, Long deviceTypeId, Long hospitalId, String deviceDepartment, Long utilizationRate) {
        DeviceStatisticsVo deviceStatisticsVo = new DeviceStatisticsVo();
        List<DeviceDetailsVo> deviceDetailsVoList = new ArrayList<>();
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        if (sysUser.getParentId() != 0) {
            //该账号为子代理
            Agent agent = agentMapper.selectAgentByUserName(sysUser.getUserName());
            List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(agent.getAgentId());
            List<Device> deviceList = new ArrayList<>();
            if (agentHospitals.size() != 0){
                agentHospitals.stream().forEach(i ->{
                    //获取子代理账号所有设别
                    List<Device> devices = deviceMapper.selectDeviceByHospitalId(i.getHospitalId());
                    deviceList.addAll(devices);
                });
            }
            deviceList.stream().forEach(map->{
                DeviceDetailsVo deviceDetailsVo = new DeviceDetailsVo();
                DeviceType deviceType = deviceTypeMapper.selectDeviceTypeByDeviceTypeId(map.getDeviceTypeId());
                Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(map.getHospitalId());
                String device_full_address = map.getDeviceFullAddress();
                String[] array = JSON.parseArray(device_full_address).toArray(new String[0]);
                Hospital deviceFloor = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[0]));
                Hospital Department = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[1]));
                Hospital deviceRoom = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[2]));
                Hospital deviceBed = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[3]));
                deviceDetailsVo.setDeviceTypeId(map.getDeviceTypeId());
                deviceDetailsVo.setDeviceNumber(map.getDeviceNumber());
                deviceDetailsVo.setDeviceTypeName(deviceType.getDeviceTypeName());
                deviceDetailsVo.setStatus(map.getStatus());
                deviceDetailsVo.setAgentId(agent.getAgentId());
                deviceDetailsVo.setAgentName(agent.getAgentName());
                deviceDetailsVo.setHospitalId(map.getHospitalId());
                deviceDetailsVo.setHospitalName(hospital.getHospitalName());
                deviceDetailsVo.setDeviceFloor(deviceFloor.getHospitalId());
                deviceDetailsVo.setDeviceDepartment(Department.getHospitalId());
                deviceDetailsVo.setDeviceRoom(deviceRoom.getHospitalId());
                deviceDetailsVo.setDeviceBed(deviceBed.getHospitalId());
                deviceDetailsVoList.add(deviceDetailsVo);
            });
            BigDecimal decimal = new BigDecimal(0);
            deviceDetailsVoList.stream().forEach(map->{
                List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectUserLeaseOrderByDeviceNumber(map.getDeviceNumber());
                userLeaseOrderList.stream().forEach(i->{
                    BigDecimal bigDecimal = new BigDecimal(i.getNetAmount());
                    decimal.add(bigDecimal);
                });
            });
            deviceStatisticsVo.setDeviceAmount(decimal);
            //根据设备类型筛选
            if (deviceTypeId!=null){
                List<DeviceDetailsVo> collect = deviceDetailsVoList.stream().filter(map -> map.getDeviceTypeId().equals(deviceTypeId)).collect(Collectors.toList());
                deviceDetailsVoList.clear();
                deviceDetailsVoList.addAll(collect);
                BigDecimal decimal1 = new BigDecimal(0);
                deviceDetailsVoList.stream().forEach(map->{
                    List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectUserLeaseOrderByDeviceNumber(map.getDeviceNumber());
                    userLeaseOrderList.stream().forEach(i->{
                        BigDecimal bigDecimal = new BigDecimal(i.getNetAmount());
                        decimal1.add(bigDecimal);
                    });
                });
                deviceStatisticsVo.setDeviceAmount(decimal);
            }
            //根据医院筛选
            if (hospitalId!=null){
                List<DeviceDetailsVo> collect = deviceDetailsVoList.stream().filter(map -> map.getHospitalId().equals(hospitalId)).collect(Collectors.toList());
                deviceDetailsVoList.clear();
                deviceDetailsVoList.addAll(collect);
                BigDecimal decimal1 = new BigDecimal(0);
                deviceDetailsVoList.stream().forEach(map->{
                    List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectUserLeaseOrderByDeviceNumber(map.getDeviceNumber());
                    userLeaseOrderList.stream().forEach(i->{
                        BigDecimal bigDecimal = new BigDecimal(i.getNetAmount());
                        decimal1.add(bigDecimal);
                    });
                });
                deviceStatisticsVo.setDeviceAmount(decimal);
            }
            //根据科室筛选
            if (deviceDepartment!=null){
                List<DeviceDetailsVo> collect = deviceDetailsVoList.stream().filter(map -> map.getDeviceDepartment().equals(deviceDepartment)).collect(Collectors.toList());
                deviceDetailsVoList.clear();
                deviceDetailsVoList.addAll(collect);
                BigDecimal decimal1 = new BigDecimal(0);
                deviceDetailsVoList.stream().forEach(map->{
                    List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectUserLeaseOrderByDeviceNumber(map.getDeviceNumber());
                    userLeaseOrderList.stream().forEach(i->{
                        BigDecimal bigDecimal = new BigDecimal(i.getNetAmount());
                        decimal1.add(bigDecimal);
                    });
                });
                deviceStatisticsVo.setDeviceAmount(decimal);
            }
            //根据使用率筛选
            /*if (utilizationRate!=null){

            }*/
            deviceStatisticsVo.setDeviceDetailsVoList(deviceDetailsVoList);
            return deviceStatisticsVo;
        } else {
            //该账号为主代理
            List<SysUser> sysUsers = sysUserMapper.selectUserByParentId(userId);
            sysUsers.add(sysUser);
            List<Device> deviceList = new ArrayList<>();
            sysUsers.stream().forEach(map -> {
                Agent agent = agentMapper.selectAgentByUserName(map.getUserName());
                List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(agent.getAgentId());
                if (agentHospitals.size() != 0) {
                    agentHospitals.stream().forEach(i -> {
                        //获取主代理及子代理的素有设备
                        List<Device> devices = deviceMapper.selectDeviceByHospitalId(i.getHospitalId());
                        deviceList.addAll(devices);
                    });
                }
                deviceList.stream().forEach(i->{
                    DeviceDetailsVo deviceDetailsVo = new DeviceDetailsVo();
                    DeviceType deviceType = deviceTypeMapper.selectDeviceTypeByDeviceTypeId(i.getDeviceTypeId());
                    Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(i.getHospitalId());
                    String device_full_address = i.getDeviceFullAddress();
                    String[] array = JSON.parseArray(device_full_address).toArray(new String[0]);
                    Hospital deviceFloor = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[0]));
                    Hospital Department = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[1]));
                    Hospital deviceRoom = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[2]));
                    Hospital deviceBed = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[3]));
                    deviceDetailsVo.setDeviceTypeId(i.getDeviceTypeId());
                    deviceDetailsVo.setDeviceNumber(i.getDeviceNumber());
                    deviceDetailsVo.setDeviceTypeName(deviceType.getDeviceTypeName());
                    deviceDetailsVo.setStatus(i.getStatus());
                    deviceDetailsVo.setAgentId(agent.getAgentId());
                    deviceDetailsVo.setAgentName(agent.getAgentName());
                    deviceDetailsVo.setHospitalId(i.getHospitalId());
                    deviceDetailsVo.setHospitalName(hospital.getHospitalName());
                    deviceDetailsVo.setDeviceFloor(deviceFloor.getHospitalId());
                    deviceDetailsVo.setDeviceDepartment(Department.getHospitalId());
                    deviceDetailsVo.setDeviceRoom(deviceRoom.getHospitalId());
                    deviceDetailsVo.setDeviceBed(deviceBed.getHospitalId());
                    deviceDetailsVoList.add(deviceDetailsVo);
                });
            });
            BigDecimal decimal = new BigDecimal(0);
            deviceDetailsVoList.stream().forEach(map->{
                List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectUserLeaseOrderByDeviceNumber(map.getDeviceNumber());
                userLeaseOrderList.stream().forEach(i->{
                    BigDecimal bigDecimal = new BigDecimal(i.getNetAmount());
                    decimal.add(bigDecimal);
                });
            });
            deviceStatisticsVo.setDeviceAmount(decimal);
            //根据设备类型筛选
            if (deviceTypeId!=null){
                List<DeviceDetailsVo> collect = deviceDetailsVoList.stream().filter(map -> map.getDeviceTypeId().equals(deviceTypeId)).collect(Collectors.toList());
                deviceDetailsVoList.clear();
                deviceDetailsVoList.addAll(collect);
                BigDecimal decimal1 = new BigDecimal(0);
                deviceDetailsVoList.stream().forEach(map->{
                    List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectUserLeaseOrderByDeviceNumber(map.getDeviceNumber());
                    userLeaseOrderList.stream().forEach(i->{
                        BigDecimal bigDecimal = new BigDecimal(i.getNetAmount());
                        decimal1.add(bigDecimal);
                    });
                });
                deviceStatisticsVo.setDeviceAmount(decimal);
            }
            //根据医院筛选
            if (hospitalId!=null){
                List<DeviceDetailsVo> collect = deviceDetailsVoList.stream().filter(map -> map.getHospitalId().equals(hospitalId)).collect(Collectors.toList());
                deviceDetailsVoList.clear();
                deviceDetailsVoList.addAll(collect);
                BigDecimal decimal1 = new BigDecimal(0);
                deviceDetailsVoList.stream().forEach(map->{
                    List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectUserLeaseOrderByDeviceNumber(map.getDeviceNumber());
                    userLeaseOrderList.stream().forEach(i->{
                        BigDecimal bigDecimal = new BigDecimal(i.getNetAmount());
                        decimal1.add(bigDecimal);
                    });
                });
                deviceStatisticsVo.setDeviceAmount(decimal);
            }
            //根据科室筛选
            if (deviceDepartment!=null){
                List<DeviceDetailsVo> collect = deviceDetailsVoList.stream().filter(map -> map.getDeviceDepartment().equals(deviceDepartment)).collect(Collectors.toList());
                deviceDetailsVoList.clear();
                deviceDetailsVoList.addAll(collect);
                BigDecimal decimal1 = new BigDecimal(0);
                deviceDetailsVoList.stream().forEach(map->{
                    List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectUserLeaseOrderByDeviceNumber(map.getDeviceNumber());
                    userLeaseOrderList.stream().forEach(i->{
                        BigDecimal bigDecimal = new BigDecimal(i.getNetAmount());
                        decimal1.add(bigDecimal);
                    });
                });
                deviceStatisticsVo.setDeviceAmount(decimal);
            }
            //根据使用率筛选
            /*if (utilizationRate!=null){

            }*/
            deviceStatisticsVo.setDeviceDetailsVoList(deviceDetailsVoList);
            return deviceStatisticsVo;
        }
    }

    @Override
    public TotalVo selectAgentRevenueStatistics(int statistics, Long userId) {
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        TotalVo totalVo = new TotalVo();
        if (sysUser.getParentId() != 0){
            TotalVo total = hospitalDeviceService.selectRevenueStatistics(sysUser.getUserName(), statistics);
            BeanUtils.copyProperties(total,totalVo);
        }else {
            List<SysUser> sysUsers = sysUserMapper.selectUserByParentId(userId);
            sysUsers.add(sysUser);
            List<OrderVo> orderVo = new ArrayList<>();
            BigDecimal orderAmount = new BigDecimal(0);
            BigDecimal dividendAmount = new BigDecimal(0);
            List<Integer> effectiveOrder = new ArrayList<>();
            sysUsers.stream().forEach(map->{
                TotalVo total = hospitalDeviceService.selectRevenueStatistics(sysUser.getUserName(), statistics);
                orderVo.addAll(total.getOrderVos());
                totalVo.setOrderAmount(orderAmount.add(total.getOrderAmount()));
                totalVo.setDividendAmount(dividendAmount.add(total.getDividendAmount()));
                effectiveOrder.add(total.getEffectiveOrder());
            });
            int sum = 0;
            for (Integer integer : effectiveOrder) {
                sum+=integer;
            }
            totalVo.setEffectiveOrder(sum);
            totalVo.setOrderVos(orderVo);
        }
        return totalVo;
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
