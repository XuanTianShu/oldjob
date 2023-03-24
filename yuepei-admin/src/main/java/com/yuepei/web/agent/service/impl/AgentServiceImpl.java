package com.yuepei.web.agent.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.utils.DateUtils;
import com.yuepei.common.utils.SecurityUtils;
import com.yuepei.system.domain.*;
import com.yuepei.system.domain.vo.*;
import com.yuepei.system.mapper.*;
import com.yuepei.system.service.HospitalDeviceService;
import com.yuepei.web.agent.service.AgentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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

    @Autowired
    private SysUserFeedbackMapper sysUserFeedbackMapper;

    @Override
    public DeviceManageVo selectAgentInfo(Long userId,Long hospitalId,Long utilizationRate) {
        DeviceManageVo manageVo = new DeviceManageVo();
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(userId);
        List<Device> devices = new ArrayList<>();
        agentHospitals.stream().forEach(map->{
            List<Device> device = deviceMapper.selectDeviceByHospitalId(map.getHospitalId());
            devices.addAll(device);
        });
        List<DeviceDetailsVo> deviceDetailsVos = new ArrayList<>();
        devices.stream().forEach(map->{
            DeviceType deviceType = deviceTypeMapper.selectDeviceTypeByDeviceTypeId(map.getDeviceTypeId());
            DeviceDetailsVo deviceDetailsVo = new DeviceDetailsVo();
            String device_full_address = map.getDeviceFullAddress();
            if (device_full_address!=null) {
                String[] array = JSON.parseArray(device_full_address).toArray(new String[0]);
                Hospital deviceFloor = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[0]));
                Hospital Department = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[1]));
                Hospital deviceRoom = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[2]));
                Hospital deviceBed = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[3]));
                deviceDetailsVo.setDeviceFloor(deviceFloor.getHospitalId());
                deviceDetailsVo.setDeviceDepartment(Department.getHospitalId());
                deviceDetailsVo.setDeviceRoom(deviceRoom.getHospitalId());
                deviceDetailsVo.setDeviceBed(deviceBed.getHospitalId());
                deviceDetailsVo.setDeviceFullAddress(device_full_address);
                deviceDetailsVo.setDeviceNumber(map.getDeviceNumber());
                deviceDetailsVo.setStatus(map.getStatus());
                deviceDetailsVo.setAgentId(sysUser.getUserId());
                deviceDetailsVo.setAgentName(sysUser.getNickName());
                Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(map.getHospitalId());
                BeanUtils.copyProperties(hospital,deviceDetailsVo);
                BeanUtils.copyProperties(deviceType,deviceDetailsVo);
                deviceDetailsVos.add(deviceDetailsVo);
            }else {
                deviceDetailsVo.setDeviceFullAddress(device_full_address);
                deviceDetailsVo.setDeviceNumber(map.getDeviceNumber());
                deviceDetailsVo.setStatus(map.getStatus());
                Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(map.getHospitalId());
                BeanUtils.copyProperties(hospital,deviceDetailsVo);
                BeanUtils.copyProperties(deviceType,deviceDetailsVo);
                deviceDetailsVos.add(deviceDetailsVo);
            }
        });
        if (hospitalId!=null){
            List<DeviceDetailsVo> collect = deviceDetailsVos.stream().filter(map -> map.getHospitalId().equals(hospitalId)).collect(Collectors.toList());
            deviceDetailsVos.clear();
            deviceDetailsVos.addAll(collect);
            Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(hospitalId);
            manageVo.setHospitalName(hospital.getHospitalName());
        }
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
        manageVo.setDeviceAmount(decimal.multiply(new BigDecimal(sysUser.getProportion())).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
        manageVo.setUtilizationRate(0L);
        manageVo.setDetailsVos(deviceDetailsVos);
        return manageVo;
    }

    @Override
    public DeviceWorkStatusVo selectDeviceDetailsByDeviceNumber(String deviceNumber) {
        Device device = deviceMapper.selectDeviceByDeviceNumber(deviceNumber);
        DeviceType deviceType = deviceTypeMapper.selectDeviceTypeByDeviceTypeId(device.getDeviceTypeId());
        DeviceWorkStatusVo workStatusVo = new DeviceWorkStatusVo();
        String device_full_address = device.getDeviceFullAddress();
        if (device_full_address!=null) {
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
            workStatusVo.setHeartbeat(new Date());
            workStatusVo.setCumulativeTime("0");
        }else {
            workStatusVo.setDeviceTypeId(deviceType.getDeviceTypeId());
            workStatusVo.setDeviceTypeName(deviceType.getDeviceTypeName());
            workStatusVo.setDeviceNumber(device.getDeviceNumber());
            workStatusVo.setDeviceAddress(device.getDeviceAddress());
            workStatusVo.setStatus(device.getStatus());
            workStatusVo.setPower(0);
            workStatusVo.setHeartbeat(new Date());
            workStatusVo.setCumulativeTime("0");
        }
        return workStatusVo;
    }

    public List<HospitalManagementVo> hospitalDevice(List<AgentHospital> agentHospitals){
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
                    HospitalManagementVo hospitalManagementVo = new HospitalManagementVo();
                    hospitalManagementVo.setHospitalId(hospital.getHospitalId());
                    hospitalManagementVo.setHospitalName(hospital.getHospitalName());
                    hospitalManagementVo.setDeviceNum(deviceList.size());
                    hospitalManagementVo.setDeviceAddress(map.getDeviceAddress());
                    hospitalManagementVo.setProportion(sysUser.getProportion());
                    hospitalManagementVos.add(hospitalManagementVo);
                }
            });
        });
        return hospitalManagementVos;
    }

    @Override
    public DeviceManageVo selectHospitalAdministration(Long userId, Long hospitalId, Long utilizationRate) {
        DeviceManageVo deviceManageVo = new DeviceManageVo();
        SysUser user = sysUserMapper.selectUserById(userId);
        List<HospitalManagementVo> hospitalManagementVos = new ArrayList<>();
        List<AgentHospital> agentHospitalsList = new ArrayList<>();
        if (user.getParentId()!=0){
            List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(user.getUserId());
            agentHospitalsList.addAll(agentHospitals);
            List<HospitalManagementVo> hospitalManagementVoList = hospitalDevice(agentHospitals);
            hospitalManagementVos.addAll(hospitalManagementVoList);
        }else {
            List<SysUser> sysUsers = sysUserMapper.selectUserByParentId(user.getUserId());
            if (sysUsers==null){
                List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(user.getUserId());
                agentHospitalsList.addAll(agentHospitals);
                List<HospitalManagementVo> hospitalManagementVoList = hospitalDevice(agentHospitals);
                hospitalManagementVos.addAll(hospitalManagementVoList);
            }else {
                sysUsers.add(user);
                sysUsers.stream().forEach(map->{
                    List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(map.getUserId());
                    agentHospitalsList.addAll(agentHospitals);
                    List<HospitalManagementVo> hospitalManagementVoList = hospitalDevice(agentHospitals);
                    hospitalManagementVos.addAll(hospitalManagementVoList);
                });
            }
        }
        List<HospitalManagementVo> managementVos = hospitalManagementVos.stream().distinct().collect(Collectors.toList());
        List<Device> devices = new ArrayList<>();
        managementVos.stream().forEach(map->{
            List<Device> deviceNumber = hospitalDeviceMapper.selectDeviceByHospitalId(map.getHospitalId());
            devices.addAll(deviceNumber);
        });
        List<Device> list = devices.stream().distinct().collect(Collectors.toList());
        List<UserLeaseOrderVo> userLeaseOrderVos = new ArrayList<>();
        list.stream().forEach(map->{
            List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectUserLeaseOrderByDeviceNumber(map.getDeviceNumber());
            userLeaseOrderList.stream().forEach(i->{
                UserLeaseOrderVo userLeaseOrderVo = new UserLeaseOrderVo();
                BeanUtils.copyProperties(i,userLeaseOrderVo);
                userLeaseOrderVo.setNetAmount(new BigDecimal(i.getNetAmount()));
                userLeaseOrderVos.add(userLeaseOrderVo);
            });
        });
        BigDecimal reduce = userLeaseOrderVos.stream().map(UserLeaseOrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        deviceManageVo.setDeviceAmount(reduce);
        if (hospitalId!=null){
            List<HospitalManagementVo> collect = managementVos.stream().filter(map -> map.getHospitalId().equals(hospitalId)).collect(Collectors.toList());
            managementVos.clear();
            managementVos.addAll(collect);
            List<UserLeaseOrderVo> userLeaseOrderVos1 = new ArrayList<>();
            list.stream().forEach(map->{
                List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectUserLeaseOrderByDeviceNumber(map.getDeviceNumber());
                userLeaseOrderList.stream().forEach(i->{
                    UserLeaseOrderVo userLeaseOrderVo = new UserLeaseOrderVo();
                    BeanUtils.copyProperties(i,userLeaseOrderVo);
                    userLeaseOrderVo.setNetAmount(new BigDecimal(i.getNetAmount()));
                    userLeaseOrderVos1.add(userLeaseOrderVo);
                });
            });
            BigDecimal reduce1 = userLeaseOrderVos1.stream().map(UserLeaseOrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            deviceManageVo.setDeviceAmount(reduce1);
        }
        deviceManageVo.setHospitalSum(agentHospitalsList.size());
        deviceManageVo.setDeviceSum(list.size());
        deviceManageVo.setUtilizationRate(0L);
        deviceManageVo.setHospitalManagementVos(managementVos);
        return deviceManageVo;
    }

    @Override
    public Long selectProportion(Long userId) {
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        if (sysUser.getParentId()!=0){
            List<SysUser> sysUsers = sysUserMapper.selectUserByParentId(userId);
            if (sysUsers==null){
                return sysUser.getProportion();
            }else {
                sysUsers.stream().forEach(map->{
                    sysUser.setProportion(sysUser.getProportion()-map.getProportion());
                });
                return sysUser.getProportion();
            }
        }else {
            List<SysUser> sysUsers = sysUserMapper.selectUserByParentId(userId);
            if (sysUsers==null){
                return sysUser.getProportion();
            }else {
                sysUsers.stream().forEach(map->{
                    sysUser.setProportion(sysUser.getProportion()-map.getProportion());
                });
                return sysUser.getProportion();
            }
        }
    }

    @Override
    @Transactional
    public String insertHospitalByAgent(HospitalAgentVo hospitalAgentVo) {
        AgentHospital agentHospital = agentMapper.selectAgentHospital(hospitalAgentVo.getHospitalId());
        SysUser su = sysUserMapper.selectUserById(hospitalAgentVo.getUserId());
        if (agentHospital!=null){
            if (agentHospital.getAgentId()==su.getUserId()){
                return "该医院已被您代理";
            }
            return "该医院已被其他代理商代理";
        }
        SysUser user = sysUserMapper.selectUserByUserName(hospitalAgentVo.getAccountNumber());
        if (user!=null){
            return "该账号已被使用,请重新输入账号";
        }
        //添加代理商和医院关联信息
        agentMapper.insertAgentHospital(user.getUserId(),hospitalAgentVo.getHospitalId());
        SysUser sysUser = new SysUser();
        //添加用户信息
        sysUser.setUserName(hospitalAgentVo.getAccountNumber());
        sysUser.setPassword(SecurityUtils.encryptPassword(hospitalAgentVo.getPassword()));
        sysUser.setNickName("医院用户");
        sysUser.setUserType("04");
        sysUser.setProportion(hospitalAgentVo.getDivided());
        sysUserMapper.insertUser(sysUser);
        //设备详细信息
        String deviceNumber = hospitalAgentVo.getDeviceNumber();
        List<String> device = Arrays.asList(deviceNumber.split(","));
        List<Device> deviceList = deviceMapper.selectDeviceByDeviceNumberList(device);
        deviceList.stream().forEach(map->{
            map.setHospitalId(hospitalAgentVo.getHospitalId());
            map.setDeviceFullAddress(null);
            deviceMapper.updateDevice(map);
        });
        return "添加成功";
    }

    public List<UserLeaseOrderVo> selectLeaseOrderList(Long userId,String deviceDepartment,String deviceTypeName,String nameOrNumber){
        List<UserLeaseOrderVo> userLeaseOrderVoList = new ArrayList<>();
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(sysUser.getUserId());
        List<UserLeaseOrder> leaseOrders = new ArrayList<>();
        agentHospitals.stream().forEach(map->{
            List<String> numberList = hospitalDeviceMapper.selectLeaseOrder(map.getHospitalId());
            if (!nameOrNumber.equals("")){
                List<UserLeaseOrder> userLeaseOrders = userLeaseOrderMapper.selectUserLeaseOrderByOrderNumber(nameOrNumber);
                numberList.stream().forEach(i->{
                    List<UserLeaseOrder> collect = userLeaseOrders.stream().filter(j -> j.getDeviceNumber().equals(i)).collect(Collectors.toList());
                    leaseOrders.addAll(collect);
                });
            }else {
                List<UserLeaseOrder> userLeaseOrders = userLeaseOrderMapper.selectUserLeaseOrder(numberList);
                leaseOrders.addAll(userLeaseOrders);
            }
        });
        List<UserLeaseOrderVo> userLeaseOrderList = leaseOrders.stream().map(a -> {
            UserLeaseOrderVo b = new UserLeaseOrderVo();
            BeanUtils.copyProperties(a, b);
            return b;
        }).collect(Collectors.toList());
        if (leaseOrders==null){
            return userLeaseOrderList;
        }
        userLeaseOrderList.stream().forEach(map->{
            Device device = hospitalDeviceMapper.selectDeviceByTypeNumber(map.getDeviceNumber());
            String deviceFullAddress = device.getDeviceFullAddress();
            if (!deviceFullAddress.isEmpty()) {
                String[] array = JSON.parseArray(deviceFullAddress).toArray(new String[0]);
                Hospital department = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[1]));
                map.setDepartment(department.getHospitalName());
            }
        });
        if (!deviceDepartment.equals("")){
            List<UserLeaseOrderVo> collect = userLeaseOrderList.stream().filter(map -> map.getDepartment().equals(deviceDepartment)).collect(Collectors.toList());
            userLeaseOrderList.clear();
            userLeaseOrderList.addAll(collect);
        }if (!deviceTypeName.equals("")){
            List<UserLeaseOrderVo> collect = userLeaseOrderList.stream().filter(map -> map.getDeviceType().equals(deviceTypeName)).collect(Collectors.toList());
            userLeaseOrderList.clear();
            userLeaseOrderList.addAll(collect);
        }
        userLeaseOrderVoList.addAll(userLeaseOrderList);
        return userLeaseOrderVoList;
    }

    @Override
    public List<UserLeaseOrderVo> selectLeaseOrder(Long userId,String deviceDepartment,String deviceTypeName,String nameOrNumber) {
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        List<UserLeaseOrderVo> userLeaseOrderVoList = new ArrayList<>();
        if (sysUser.getParentId()!=0){
            List<UserLeaseOrderVo> userLeaseOrderVos = selectLeaseOrderList(userId, deviceDepartment, deviceTypeName, nameOrNumber);
            userLeaseOrderVoList.addAll(userLeaseOrderVos);
        }else {
            List<SysUser> sysUsers = sysUserMapper.selectUserByParentId(userId);
            if (sysUsers==null){
                List<UserLeaseOrderVo> userLeaseOrderVos = selectLeaseOrderList(userId, deviceDepartment, deviceTypeName, nameOrNumber);
                userLeaseOrderVoList.addAll(userLeaseOrderVos);
            }else {
                sysUsers.add(sysUser);
                sysUsers.stream().forEach(map->{
                    List<UserLeaseOrderVo> userLeaseOrderVos = selectLeaseOrderList(map.getUserId(), deviceDepartment, deviceTypeName, nameOrNumber);
                    if (userLeaseOrderVos!=null){
                        userLeaseOrderVoList.addAll(userLeaseOrderVos);
                    }
                });
            }
        }
        List<UserLeaseOrderVo> collect = userLeaseOrderVoList.stream().sorted(Comparator.comparing(UserLeaseOrderVo::getLeaseTime).reversed()).collect(Collectors.toList());
        return collect;
    }

    @Override
    public String selectAgentByUser(Long userId){
        SysUser user = sysUserMapper.selectUserById(userId);
        return user.getNickName();
    }

    @Override
    @Transactional
    public String insertAgentAccount(SubAccountVo subAccountVo) {
        List<SysUser> user = sysUserMapper.selectUserByParentId(subAccountVo.getUserId());
        if (user.size()>=2){
            return "您只能添加两个子账户";
        }
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
        sysUser.setParentId(subAccountVo.getUserId());
        sysUser.setNickName(subAccountVo.getAgentName());
        sysUserMapper.insertUser(sysUser);
        return "添加成功";
    }

    @Override
    public List<SubAccountManageVo> selectSubAccount(Long userId) {
        List<SysUser> sysUsers = sysUserMapper.selectUserByParentId(userId);
        List<SubAccountManageVo> subAccountManageVoList = new ArrayList<>();
        sysUsers.stream().forEach(map->{
            SubAccountManageVo subAccountManageVo = new SubAccountManageVo();
            List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(map.getUserId());
            subAccountManageVo.setProportion(map.getProportion());
            subAccountManageVo.setAgentName(map.getNickName());
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
            List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(sysUser.getUserId());
            List<Device> deviceList = new ArrayList<>();
            agentHospitals.stream().forEach(map->{
                List<Device> devices = hospitalDeviceMapper.selectDeviceByHospitalId(map.getHospitalId());
                deviceList.addAll(devices);
            });
            List<DeviceType> deviceTypeList = new ArrayList<>();
            deviceList.stream().forEach(map->{
                DeviceType deviceType = hospitalDeviceMapper.selectDeviceByTypeName(map.getDeviceTypeId());
                deviceTypeList.add(deviceType);
            });
            List<DeviceType> collect = deviceTypeList.stream().distinct().collect(Collectors.toList());
            List<UserLeaseOrder> userLeaseOrderList = new ArrayList<>();
            agentDeviceVo.setHospitalSum(agentHospitals.size());
            if (agentHospitals.size()!=0){
                deviceList.stream().forEach(map->{
                    UserLeaseOrder userLeaseOrder = userLeaseOrderMapper.selectUseDevice(map.getDeviceNumber());
                    userLeaseOrderList.add(userLeaseOrder);
                });
            }
            agentDeviceVo.setHospitalSum(agentHospitals.size());
            agentDeviceVo.setDeviceSum(deviceList.size());
            agentDeviceVo.setUseDeviceSum(userLeaseOrderList.size());
            agentDeviceVo.setDeviceTypes(collect);
            return agentDeviceVo;
        }else {
            //该账号为主代理
            List<SysUser> sysUsers = sysUserMapper.selectUserByParentId(userId);
            if (sysUsers==null){
                //该账号没有子代理
                List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(sysUser.getUserId());
                List<Device> deviceList = new ArrayList<>();
                agentHospitals.stream().forEach(map->{
                    List<Device> devices = hospitalDeviceMapper.selectDeviceByHospitalId(map.getHospitalId());
                    deviceList.addAll(devices);
                });
                List<DeviceType> deviceTypeList = new ArrayList<>();
                deviceList.stream().forEach(map->{
                    DeviceType deviceType = hospitalDeviceMapper.selectDeviceByTypeName(map.getDeviceTypeId());
                    deviceTypeList.add(deviceType);
                });
                List<DeviceType> collect = deviceTypeList.stream().distinct().collect(Collectors.toList());
                List<UserLeaseOrder> userLeaseOrderList = new ArrayList<>();
                agentDeviceVo.setHospitalSum(agentHospitals.size());
                if (agentHospitals.size()!=0){
                    deviceList.stream().forEach(map->{
                        UserLeaseOrder userLeaseOrder = userLeaseOrderMapper.selectUseDevice(map.getDeviceNumber());
                        userLeaseOrderList.add(userLeaseOrder);
                    });
                }
                agentDeviceVo.setHospitalSum(agentHospitals.size());
                agentDeviceVo.setDeviceSum(deviceList.size());
                agentDeviceVo.setUseDeviceSum(userLeaseOrderList.size());
                agentDeviceVo.setDeviceTypes(collect);
                return agentDeviceVo;
            }else {

                sysUsers.add(sysUser);
                List<Device> deviceList = new ArrayList<>();
                List<AgentHospital> agentHospitalList = new ArrayList<>();
                List<UserLeaseOrder> userLeaseOrderList = new ArrayList<>();
                List<DeviceType> deviceTypeList = new ArrayList<>();
                sysUsers.stream().forEach(map->{
                    List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(map.getUserId());
                    agentHospitalList.addAll(agentHospitals);
                    agentHospitals.stream().forEach(i->{
                        List<Device> devices = hospitalDeviceMapper.selectDeviceByHospitalId(i.getHospitalId());
                        deviceList.addAll(devices);
                    });
                });
                deviceList.stream().forEach(i->{
                    DeviceType deviceType = hospitalDeviceMapper.selectDeviceByTypeName(i.getDeviceTypeId());
                    deviceTypeList.add(deviceType);
                });
                List<DeviceType> collect = deviceTypeList.stream().distinct().collect(Collectors.toList());
                agentDeviceVo.setHospitalSum(agentHospitalList.size());
                if (agentHospitalList.size()!=0){
                    deviceList.stream().forEach(i->{
                        UserLeaseOrder userLeaseOrder = userLeaseOrderMapper.selectUseDevice(i.getDeviceNumber());
                        if (userLeaseOrder!=null){
                            userLeaseOrderList.add(userLeaseOrder);
                        }
                    });
                }
                List<UserLeaseOrder> userLeaseOrders = userLeaseOrderList.stream().distinct().collect(Collectors.toList());
                agentDeviceVo.setHospitalSum(agentHospitalList.size());
                agentDeviceVo.setDeviceSum(collect.size());
                agentDeviceVo.setUseDeviceSum(userLeaseOrders.size());
                agentDeviceVo.setDeviceTypes(collect);
                return agentDeviceVo;
            }
        }
    }

    @Override
    public List<DeviceType> selectDeviceList(Long userId) {
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(sysUser.getUserId());
        List<Device> deviceList = new ArrayList<>();
        List<DeviceType> deviceNumber = new ArrayList<>();
        agentHospitals.stream().forEach(map->{
            List<Device> devices = deviceMapper.selectDeviceByHospitalId(map.getHospitalId());
            deviceList.addAll(devices);
        });
        deviceList.stream().forEach(map->{
            DeviceType deviceType = deviceTypeMapper.selectDeviceTypeByDeviceTypeId(map.getDeviceTypeId());
            deviceNumber.add(deviceType);
        });
        return deviceNumber;
    }

    @Override
    public DeviceStatisticsVo selectDeviceTypeDetails(Long userId, Long deviceTypeId, Long hospitalId, String deviceDepartment, Long utilizationRate) {
        DeviceStatisticsVo deviceStatisticsVo = new DeviceStatisticsVo();
        List<DeviceDetailsVo> deviceDetailsVoList = new ArrayList<>();
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        if (sysUser.getParentId() != 0) {
            //该账号为子代理
            List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(sysUser.getUserId());
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
                if (device_full_address!=null){
                    String[] array = JSON.parseArray(device_full_address).toArray(new String[0]);
                    Hospital deviceFloor = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[0]));
                    Hospital Department = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[1]));
                    Hospital deviceRoom = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[2]));
                    Hospital deviceBed = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[3]));
                    deviceDetailsVo.setDeviceTypeId(map.getDeviceTypeId());
                    deviceDetailsVo.setDeviceNumber(map.getDeviceNumber());
                    deviceDetailsVo.setDeviceTypeName(deviceType.getDeviceTypeName());
                    deviceDetailsVo.setStatus(map.getStatus());
                    deviceDetailsVo.setAgentId(sysUser.getUserId());
                    deviceDetailsVo.setAgentName(sysUser.getNickName());
                    deviceDetailsVo.setHospitalId(map.getHospitalId());
                    deviceDetailsVo.setHospitalName(hospital.getHospitalName());
                    deviceDetailsVo.setDeviceFloor(deviceFloor.getHospitalId());
                    deviceDetailsVo.setDeviceFloorName(deviceFloor.getHospitalName());
                    deviceDetailsVo.setDeviceDepartment(Department.getHospitalId());
                    deviceDetailsVo.setDeviceDepartmentName(Department.getHospitalName());
                    deviceDetailsVo.setDeviceRoom(deviceRoom.getHospitalId());
                    deviceDetailsVo.setDeviceRoomName(deviceRoom.getHospitalName());
                    deviceDetailsVo.setDeviceBed(deviceBed.getHospitalId());
                    deviceDetailsVo.setDeviceBedName(deviceBed.getHospitalName());
                    deviceDetailsVoList.add(deviceDetailsVo);
                }else {
                    deviceDetailsVo.setDeviceTypeId(map.getDeviceTypeId());
                    deviceDetailsVo.setDeviceNumber(map.getDeviceNumber());
                    deviceDetailsVo.setDeviceTypeName(deviceType.getDeviceTypeName());
                    deviceDetailsVo.setStatus(map.getStatus());
                    deviceDetailsVo.setAgentId(sysUser.getUserId());
                    deviceDetailsVo.setAgentName(sysUser.getNickName());
                    deviceDetailsVo.setHospitalId(map.getHospitalId());
                    deviceDetailsVo.setHospitalName(hospital.getHospitalName());
                    deviceDetailsVoList.add(deviceDetailsVo);
                }
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
            if (!deviceDepartment.equals("")){
                List<DeviceDetailsVo> collect = deviceDetailsVoList.stream()
                        .filter(map -> map.getDeviceDepartmentName()!=null)
                        .filter(map -> map.getDeviceDepartmentName().equals(deviceDepartment))
                        .collect(Collectors.toList());
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
            if (sysUsers==null){
                List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(sysUser.getUserId());
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
                    if (device_full_address!=null){
                        String[] array = JSON.parseArray(device_full_address).toArray(new String[0]);
                        Hospital deviceFloor = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[0]));
                        Hospital Department = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[1]));
                        Hospital deviceRoom = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[2]));
                        Hospital deviceBed = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[3]));
                        deviceDetailsVo.setDeviceTypeId(map.getDeviceTypeId());
                        deviceDetailsVo.setDeviceNumber(map.getDeviceNumber());
                        deviceDetailsVo.setDeviceTypeName(deviceType.getDeviceTypeName());
                        deviceDetailsVo.setStatus(map.getStatus());
                        deviceDetailsVo.setAgentId(sysUser.getUserId());
                        deviceDetailsVo.setAgentName(sysUser.getNickName());
                        deviceDetailsVo.setHospitalId(map.getHospitalId());
                        deviceDetailsVo.setHospitalName(hospital.getHospitalName());
                        deviceDetailsVo.setDeviceFloor(deviceFloor.getHospitalId());
                        deviceDetailsVo.setDeviceFloorName(deviceFloor.getHospitalName());
                        deviceDetailsVo.setDeviceDepartment(Department.getHospitalId());
                        deviceDetailsVo.setDeviceDepartmentName(Department.getHospitalName());
                        deviceDetailsVo.setDeviceRoom(deviceRoom.getHospitalId());
                        deviceDetailsVo.setDeviceRoomName(deviceRoom.getHospitalName());
                        deviceDetailsVo.setDeviceBed(deviceBed.getHospitalId());
                        deviceDetailsVo.setDeviceBedName(deviceBed.getHospitalName());
                        deviceDetailsVoList.add(deviceDetailsVo);
                    }else {
                        deviceDetailsVo.setDeviceTypeId(map.getDeviceTypeId());
                        deviceDetailsVo.setDeviceNumber(map.getDeviceNumber());
                        deviceDetailsVo.setDeviceTypeName(deviceType.getDeviceTypeName());
                        deviceDetailsVo.setStatus(map.getStatus());
                        deviceDetailsVo.setAgentId(sysUser.getUserId());
                        deviceDetailsVo.setAgentName(sysUser.getNickName());
                        deviceDetailsVo.setHospitalId(map.getHospitalId());
                        deviceDetailsVo.setHospitalName(hospital.getHospitalName());
                        deviceDetailsVoList.add(deviceDetailsVo);
                    }
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
                    List<DeviceDetailsVo> collect = deviceDetailsVoList.stream()
                            .filter(map -> map.getDeviceDepartmentName()!=null)
                            .filter(map -> map.getDeviceDepartment().equals(deviceDepartment))
                            .collect(Collectors.toList());
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
            }else {
                sysUsers.add(sysUser);
                sysUsers.stream().forEach(map -> {
                    List<Device> deviceList = new ArrayList<>();
                    List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(map.getUserId());
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
                        if (device_full_address!=null){
                            String[] array = JSON.parseArray(device_full_address).toArray(new String[0]);
                            Hospital deviceFloor = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[0]));
                            Hospital Department = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[1]));
                            Hospital deviceRoom = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[2]));
                            Hospital deviceBed = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[3]));
                            deviceDetailsVo.setDeviceFloor(deviceFloor.getHospitalId());
                            deviceDetailsVo.setDeviceFloorName(deviceFloor.getHospitalName());
                            deviceDetailsVo.setDeviceDepartment(Department.getHospitalId());
                            deviceDetailsVo.setDeviceDepartmentName(Department.getHospitalName());
                            deviceDetailsVo.setDeviceRoom(deviceRoom.getHospitalId());
                            deviceDetailsVo.setDeviceRoomName(deviceRoom.getHospitalName());
                            deviceDetailsVo.setDeviceBed(deviceBed.getHospitalId());
                            deviceDetailsVo.setDeviceBedName(deviceBed.getHospitalName());
                            deviceDetailsVo.setDeviceTypeId(i.getDeviceTypeId());
                            deviceDetailsVo.setDeviceNumber(i.getDeviceNumber());
                            deviceDetailsVo.setDeviceTypeName(deviceType.getDeviceTypeName());
                            deviceDetailsVo.setStatus(i.getStatus());
                            deviceDetailsVo.setAgentId(sysUser.getUserId());
                            deviceDetailsVo.setAgentName(sysUser.getNickName());
                            deviceDetailsVo.setHospitalId(i.getHospitalId());
                            deviceDetailsVo.setHospitalName(hospital.getHospitalName());
                            deviceDetailsVoList.add(deviceDetailsVo);
                        }else {
                            deviceDetailsVo.setDeviceTypeId(i.getDeviceTypeId());
                            deviceDetailsVo.setDeviceNumber(i.getDeviceNumber());
                            deviceDetailsVo.setDeviceTypeName(deviceType.getDeviceTypeName());
                            deviceDetailsVo.setStatus(i.getStatus());
                            deviceDetailsVo.setAgentId(sysUser.getUserId());
                            deviceDetailsVo.setAgentName(sysUser.getNickName());
                            deviceDetailsVo.setHospitalId(i.getHospitalId());
                            deviceDetailsVo.setHospitalName(hospital.getHospitalName());
                            deviceDetailsVoList.add(deviceDetailsVo);
                        }
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
                if (!deviceDepartment.equals("")){
                    List<DeviceDetailsVo> collect = deviceDetailsVoList.stream()
                            .filter(map -> map.getDeviceDepartmentName()!=null)
                            .filter(map -> map.getDeviceDepartmentName().equals(deviceDepartment))
                            .collect(Collectors.toList());
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
    }

    public TotalVo revenueStatistics(SysUser user,int statistics){
        List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(user.getUserId());
        List<String> deviceNumberList = new ArrayList<>();
        agentHospitals.stream().forEach(map->{
            List<String> deviceNumbers = hospitalDeviceMapper.selectLeaseOrder(map.getHospitalId());
            deviceNumberList.addAll(deviceNumbers);
        });
        if (deviceNumberList.size()==0){
            return null;
        }
        SysUser sysUser = sysUserMapper.selectUserByUserName(user.getUserName());
        TotalVo totalVo = new TotalVo();
        List<OrderVo> orderVos = new ArrayList<>();
        if (statistics == 1) {
            List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectRevenueStatistics(deviceNumberList);
            Date dNow = new Date();   //当前时间
            Date dBefore = new Date();
            Calendar calendar = Calendar.getInstance(); //得到日历
            calendar.setTime(dNow);//把当前时间赋给日历
            calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
            dBefore = calendar.getTime();   //得到前一天的时间
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
            String defaultStartDate = sdf.format(dBefore);    //格式化前一天
            String now = sdf.format(dNow);
            List<UserLeaseOrder> userLeaseOrders = new ArrayList<>();
            try {
                //使用SimpleDateFormat的parse()方法生成Date
                Date date = sdf.parse(defaultStartDate);
                Date parse = sdf.parse(now);
                List<UserLeaseOrder> userLeaseOrder = userLeaseOrderList.stream()
                        .filter(s->s.getLeaseTime().getTime()<parse.getTime())
                        .filter(s->s.getLeaseTime().getTime()>date.getTime())
                        .collect(Collectors.toList());
                userLeaseOrders.addAll(userLeaseOrder);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            userLeaseOrders.stream().forEach(map->{
                OrderVo orderVo = new OrderVo();
                orderVo.setOrderNumber(map.getOrderNumber());
                BigDecimal decimal = new BigDecimal(map.getNetAmount());
                orderVo.setNetAmount(decimal);
                orderVo.setDividendRatio(sysUser.getProportion());
                orderVo.setIncomeAmount(decimal.multiply(new BigDecimal(sysUser.getProportion())).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
                orderVos.add(orderVo);
            });
            totalVo.setEffectiveOrder(orderVos.size());
            BigDecimal orderAmount = orderVos.stream().map(OrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal dividendAmount = orderVos.stream().map(OrderVo::getIncomeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            totalVo.setOrderAmount(orderAmount);
            totalVo.setDividendAmount(dividendAmount);
            totalVo.setOrderVos(orderVos);
        } else if (statistics == 2) {
            List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectRevenueStatistics(deviceNumberList);
            Date dNow = new Date();   //当前时间
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
            String format = sdf.format(dNow);
            List<UserLeaseOrder> userLeaseOrders = new ArrayList<>();
            try {
                //使用SimpleDateFormat的parse()方法生成Date
                Date date = sdf.parse(format);
                List<UserLeaseOrder> userLeaseOrder = userLeaseOrderList.stream()
                        .filter(s->s.getLeaseTime().getTime()>=date.getTime())
                        .collect(Collectors.toList());
                userLeaseOrders.addAll(userLeaseOrder);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            userLeaseOrders.stream().forEach(map->{
                OrderVo orderVo = new OrderVo();
                orderVo.setOrderNumber(map.getOrderNumber());
                BigDecimal decimal = new BigDecimal(map.getNetAmount());
                orderVo.setNetAmount(decimal);
                orderVo.setDividendRatio(sysUser.getProportion());
                orderVo.setIncomeAmount(decimal.multiply(new BigDecimal(sysUser.getProportion())).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
                orderVos.add(orderVo);
            });
            totalVo.setEffectiveOrder(orderVos.size());
            BigDecimal orderAmount = orderVos.stream().map(OrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal dividendAmount = orderVos.stream().map(OrderVo::getIncomeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            totalVo.setOrderAmount(orderAmount);
            totalVo.setDividendAmount(dividendAmount);
            totalVo.setOrderVos(orderVos);
        } else if (statistics == 3) {
            List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectRevenueStatistics(deviceNumberList);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            // 获取前月的第一天
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            String firstDay = format.format(cale.getTime());
            // 获取前月的最后一天
            cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 1);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            String lastDay = format.format(cale.getTime());
            List<UserLeaseOrder> userLeaseOrders = new ArrayList<>();
            try {
                //使用SimpleDateFormat的parse()方法生成Date
                Date first = format.parse(firstDay);
                Date last = format.parse(lastDay);
                List<UserLeaseOrder> userLeaseOrder = userLeaseOrderList.stream()
                        .filter(s->s.getLeaseTime().getTime()>=first.getTime())
                        .filter(s->s.getLeaseTime().getTime()<=last.getTime())
                        .collect(Collectors.toList());
                userLeaseOrders.addAll(userLeaseOrder);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            userLeaseOrders.stream().forEach(map->{
                OrderVo orderVo = new OrderVo();
                orderVo.setOrderNumber(map.getOrderNumber());
                BigDecimal decimal = new BigDecimal(map.getNetAmount());
                orderVo.setNetAmount(decimal);
                orderVo.setDividendRatio(sysUser.getProportion());
                orderVo.setIncomeAmount(decimal.multiply(new BigDecimal(sysUser.getProportion())).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
                orderVos.add(orderVo);
            });
            totalVo.setEffectiveOrder(orderVos.size());
            BigDecimal orderAmount = orderVos.stream().map(OrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal dividendAmount = orderVos.stream().map(OrderVo::getIncomeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            totalVo.setOrderAmount(orderAmount);
            totalVo.setDividendAmount(dividendAmount);
            totalVo.setOrderVos(orderVos);
        } else {
            List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectRevenueStatistics(deviceNumberList);
            userLeaseOrderList.stream().forEach(map->{
                OrderVo orderVo = new OrderVo();
                orderVo.setOrderNumber(map.getOrderNumber());
                BigDecimal decimal = new BigDecimal(map.getNetAmount());
                orderVo.setNetAmount(decimal);
                orderVo.setDividendRatio(sysUser.getProportion());
                orderVo.setIncomeAmount(decimal.multiply(new BigDecimal(sysUser.getProportion())).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
                orderVos.add(orderVo);
            });
            totalVo.setEffectiveOrder(orderVos.size());
            BigDecimal orderAmount = orderVos.stream().map(OrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal dividendAmount = orderVos.stream().map(OrderVo::getIncomeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            totalVo.setOrderAmount(orderAmount);
            totalVo.setDividendAmount(dividendAmount);
            totalVo.setOrderVos(orderVos);
        }
        return totalVo;
    }

    @Override
    public TotalVo selectAgentRevenueStatistics(int statistics, Long userId) {
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        TotalVo totalVo = new TotalVo();
        if (sysUser.getParentId() != 0){
            List<OrderVo> orderVo = new ArrayList<>();
            BigDecimal orderAmount = new BigDecimal(0);
            BigDecimal dividendAmount = new BigDecimal(0);
            List<Integer> effectiveOrder = new ArrayList<>();
            TotalVo total = revenueStatistics(sysUser,statistics);
            BeanUtils.copyProperties(total,totalVo);
            orderVo.addAll(total.getOrderVos());
            totalVo.setOrderAmount(orderAmount.add(total.getOrderAmount()));
            totalVo.setDividendAmount(dividendAmount.add(total.getDividendAmount()));
            effectiveOrder.add(total.getEffectiveOrder());
            int sum = 0;
            for (Integer integer : effectiveOrder) {
                sum+=integer;
            }
            totalVo.setEffectiveOrder(sum);
            totalVo.setOrderVos(orderVo);
        }else {
            List<SysUser> sysUsers = sysUserMapper.selectUserByParentId(userId);
            if (sysUsers==null){
                List<OrderVo> orderVo = new ArrayList<>();
                BigDecimal orderAmount = new BigDecimal(0);
                BigDecimal dividendAmount = new BigDecimal(0);
                List<Integer> effectiveOrder = new ArrayList<>();
                TotalVo total = revenueStatistics(sysUser,statistics);
                BeanUtils.copyProperties(total,totalVo);
                orderVo.addAll(total.getOrderVos());
                totalVo.setOrderAmount(orderAmount.add(total.getOrderAmount()));
                totalVo.setDividendAmount(dividendAmount.add(total.getDividendAmount()));
                effectiveOrder.add(total.getEffectiveOrder());
                int sum = 0;
                for (Integer integer : effectiveOrder) {
                    sum+=integer;
                }
                totalVo.setEffectiveOrder(sum);
                totalVo.setOrderVos(orderVo);
            }else {
                sysUsers.add(sysUser);
                List<OrderVo> orderVo = new ArrayList<>();
                BigDecimal orderAmount = new BigDecimal(0);
                BigDecimal dividendAmount = new BigDecimal(0);
                List<Integer> effectiveOrder = new ArrayList<>();
                sysUsers.stream().forEach(map->{
                    TotalVo total = revenueStatistics(map,statistics);
                    if (total!=null){
                        orderVo.addAll(total.getOrderVos());
                        totalVo.setOrderAmount(orderAmount.add(total.getOrderAmount()));
                        totalVo.setDividendAmount(dividendAmount.add(total.getDividendAmount()));
                        effectiveOrder.add(total.getEffectiveOrder());
                    }
                });
                int sum = 0;
                for (Integer integer : effectiveOrder) {
                    sum+=integer;
                }
                totalVo.setEffectiveOrder(sum);
                totalVo.setOrderVos(orderVo);
            }
        }
        return totalVo;
    }

    public List<String> selectDepartmentDetails(Long userId){
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        List<AgentHospital> agentHospitalList = agentMapper.selectAgentHospitalByHospital(sysUser.getUserId());
        List<String> deviceDepartment = new ArrayList<>();
        agentHospitalList.stream().forEach(map->{
            List<Device> deviceList = hospitalDeviceMapper.selectDeviceByHospitalId(map.getHospitalId());
            deviceList.stream().forEach(i -> {
                String device_full_address = i.getDeviceFullAddress();
                if (device_full_address!=null) {
                    String[] array = JSON.parseArray(device_full_address).toArray(new String[0]);
                    Hospital Department = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[1]));
                    deviceDepartment.add(Department.getHospitalName());
                }
            });
        });
        List<String> collect = deviceDepartment.stream().distinct().collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<String> selectDepartment(Long userId) {
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        ArrayList<String> departmentList = new ArrayList<>();
        List<String> stringList = selectDepartmentDetails(sysUser.getUserId());
        departmentList.addAll(stringList);
        return departmentList;
    }

    @Override
    public List<Hospital> selectHospitalList() {
        return hospitalDeviceMapper.selectHospitalList();
    }

    @Override
    public List<FeedbackInfoVo> selectDeviceFaultList(Long userId,Integer status,String numberOrAddress) {
        List<FeedbackInfoVo> feedbackInfoVoList = new ArrayList<>();
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(sysUser.getUserId());
        List<Device> deviceList = new ArrayList<>();
        if (numberOrAddress.equals("")){
            agentHospitals.stream().forEach(map->{
                List<Device> devices = hospitalDeviceMapper.selectDeviceByHospitalId(map.getHospitalId());
                deviceList.addAll(devices);
            });
        }else {
            agentHospitals.stream().forEach(map->{
                List<Device> devices = hospitalDeviceMapper.selectDeviceByHospitalIdLike(map.getHospitalId(),numberOrAddress);
                deviceList.addAll(devices);
            });
        }
        List<SysUserFeedback> sysUserFeedbackList = new ArrayList<>();
        deviceList.stream().forEach(map->{
            List<SysUserFeedback> sysUserFeedbacks = sysUserFeedbackMapper.selectDeviceFaultList(map.getDeviceNumber(),status);
            sysUserFeedbackList.addAll(sysUserFeedbacks);
        });
        sysUserFeedbackList.stream().forEach(map->{
            FeedbackInfoVo feedbackInfoVo = new FeedbackInfoVo();
            List<String> feedback = new ArrayList<>();
            BeanUtils.copyProperties(map,feedbackInfoVo);
            String feedbackUrl = map.getFeedbackUrl();
            Device device = hospitalDeviceMapper.selectDeviceByTypeNumber(map.getDeviceNumber());
            List<List<String>> lists = JSON.parseObject(feedbackUrl, new TypeReference<List<List<String>>>() {
            });
            for (List<String> list : lists) {
                for (String s : list) {
                    feedback.add(s);
                }
            }
            feedbackInfoVo.setDeviceAddress(device.getDeviceAddress());
            feedbackInfoVo.setFeedbackUrl(feedback);
            feedbackInfoVoList.add(feedbackInfoVo);
        });
        return feedbackInfoVoList;
    }

    @Override
    public List<FeedbackInfoVo> selectDeviceFaultDetails(Long userId, Integer status, Long feedbackId) {
        List<FeedbackInfoVo> feedbackInfoVoList = new ArrayList<>();
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        List<AgentHospital> agentHospitals = agentMapper.selectAgentHospitalByHospital(sysUser.getUserId());
        List<Device> deviceList = new ArrayList<>();
        agentHospitals.stream().forEach(map->{
            List<Device> devices = hospitalDeviceMapper.selectDeviceByHospitalId(map.getHospitalId());
            deviceList.addAll(devices);
        });
        List<SysUserFeedback> sysUserFeedbackList = new ArrayList<>();
        deviceList.stream().forEach(map->{
            List<SysUserFeedback> sysUserFeedbacks = sysUserFeedbackMapper.selectDeviceFaultList(map.getDeviceNumber(),status);
            sysUserFeedbackList.addAll(sysUserFeedbacks);
        });
        sysUserFeedbackList.stream().forEach(map->{
            FeedbackInfoVo feedbackInfoVo = new FeedbackInfoVo();
            List<String> feedback = new ArrayList<>();
            BeanUtils.copyProperties(map,feedbackInfoVo);
            String feedbackUrl = map.getFeedbackUrl();
            Device device = hospitalDeviceMapper.selectDeviceByTypeNumber(map.getDeviceNumber());
            List<List<String>> lists = JSON.parseObject(feedbackUrl, new TypeReference<List<List<String>>>() {
            });
            for (List<String> list : lists) {
                for (String s : list) {
                    feedback.add(s);
                }
            }
            feedbackInfoVo.setDeviceAddress(device.getDeviceAddress());
            feedbackInfoVo.setFeedbackUrl(feedback);
            feedbackInfoVoList.add(feedbackInfoVo);
        });
        List<FeedbackInfoVo> collect = feedbackInfoVoList.stream().filter(map -> map.getFeedbackId().equals(feedbackId)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public int writeMaintenanceRecords(FeedbackInfoVo feedback) {
        SysUserFeedback sysUserFeedback = sysUserFeedbackMapper.selectSysUserFeedbackById(feedback.getFeedbackId());
        sysUserFeedback.setFeedbackDescribe(feedback.getFeedbackDescribe());
        List<String> devicePicture = feedback.getDevicePicture();
        String urls = JSON.toJSONString(devicePicture);
        List<List<String>> lists = JSON.parseObject(urls, new TypeReference<List<List<String>>>() {
        });
        String string = JSON.toJSONString(lists);
        sysUserFeedback.setDevicePicture(string);
        sysUserFeedback.setStatus(1);
        sysUserFeedback.setFeedbackTime(DateUtils.getDate());
        return sysUserFeedbackMapper.updateSysUserFeedbackById(sysUserFeedback);
    }

    @Override
    public FeedbackInfoVo feedbackRepairCompleted(Long feedbackId) {
        SysUserFeedback sysUserFeedback = sysUserFeedbackMapper.selectSysUserFeedbackById(feedbackId);
        Device device = deviceMapper.selectDeviceByDeviceNumber(sysUserFeedback.getDeviceNumber());
        FeedbackInfoVo feedbackInfoVo = new FeedbackInfoVo();
        BeanUtils.copyProperties(sysUserFeedback,feedbackInfoVo);
        feedbackInfoVo.setDeviceAddress(device.getDeviceAddress());
        String devicePicture = sysUserFeedback.getDevicePicture();
        List<List<String>> lists = JSON.parseObject(devicePicture, new TypeReference<List<List<String>>>() {
        });
        List<String> arrayList = new ArrayList<>();
        for (List<String> list : lists) {
            for (String s : list) {
                arrayList.add(s);
            }
        }
        feedbackInfoVo.setDevicePicture(arrayList);
        return feedbackInfoVo;
    }

    @Override
    public int uploadsFile(FeedbackInfoVo feedbackInfoVo) {
        SysUser sysUser = sysUserMapper.selectUserById(feedbackInfoVo.getUserId());
        SysUserFeedback feedback = new SysUserFeedback();
        feedback.setUserId(sysUser.getUserId());
        feedback.setUserName(sysUser.getUserName());
        feedback.setPhoneNumber(sysUser.getPhoneNumber());
        String urls = JSON.toJSONString(feedbackInfoVo.getFeedbackUrl());
        List<List<String>> lists = JSON.parseObject(urls, new TypeReference<List<List<String>>>() {
        });
        String string = JSON.toJSONString(lists);
        feedback.setFeedbackUrl(string);
        feedback.setFeedbackTime(DateUtils.getTime());
        feedback.setFeedbackType("3");
        feedback.setStatus(0);
        return sysUserFeedbackMapper.insertSysUserFeedback(feedback);
    }

    @Override
    public List<FeedbackInfoVo> selectUploadsFileList(Long userId) {
        List<FeedbackInfoVo> infoVoList = new ArrayList<>();
        List<SysUserFeedback> userFeedbackList = sysUserFeedbackMapper.selectSysUserFeedbackByUserId(userId);
        userFeedbackList.stream().forEach(map->{
            FeedbackInfoVo feedbackInfoVo = new FeedbackInfoVo();
            BeanUtils.copyProperties(map,feedbackInfoVo);
            List<List<String>> lists = JSON.parseObject(map.getFeedbackUrl(), new TypeReference<List<List<String>>>() {
            });
            List<String> arrayList = new ArrayList<>();
            for (List<String> list : lists) {
                for (String s : list) {
                    arrayList.add(s);
                }
            }
            feedbackInfoVo.setFeedbackUrl(arrayList);
            infoVoList.add(feedbackInfoVo);
        });
        return infoVoList;
    }

    @Override
    public FeedbackInfoVo selectUploadsFileListDetails(Long feedbackId) {
        SysUserFeedback sysUserFeedback = sysUserFeedbackMapper.selectSysUserFeedbackById(feedbackId);
        FeedbackInfoVo feedbackInfoVo = new FeedbackInfoVo();
        BeanUtils.copyProperties(sysUserFeedback,feedbackInfoVo);
        List<List<String>> lists = JSON.parseObject(sysUserFeedback.getFeedbackUrl(), new TypeReference<List<List<String>>>() {
        });
        List<String> arrayList = new ArrayList<>();
        for (List<String> list : lists) {
            for (String s : list) {
                arrayList.add(s);
            }
        }
        feedbackInfoVo.setFeedbackUrl(arrayList);
        return feedbackInfoVo;
    }

    @Override
    public List<Hospital> selectAgentHospitalList(Long userId) {
        List<AgentHospital> agentHospitalList = agentMapper.selectAgentHospitalByHospital(userId);
        List<Hospital> hospitals = new ArrayList<>();
        agentHospitalList.stream().forEach(map->{
            Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(map.getHospitalId());
            hospitals.add(hospital);
        });
        return hospitals;
    }
}
