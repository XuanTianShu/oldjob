package com.yuepei.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.google.gson.Gson;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.utils.DateUtils;
import com.yuepei.common.utils.SecurityUtils;
import com.yuepei.common.utils.ServletUtils;
import com.yuepei.common.utils.ip.IpUtils;
import com.yuepei.system.domain.*;
import com.yuepei.system.domain.vo.*;
import com.yuepei.system.mapper.AgentMapper;
import com.yuepei.system.mapper.HospitalDeviceMapper;
import com.yuepei.system.mapper.SysUserMapper;
import com.yuepei.system.mapper.UserLeaseOrderMapper;
import com.yuepei.system.service.ISysUserService;
import com.yuepei.system.service.HospitalDeviceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zzy
 * @date 2023/2/9 16:38
 */
@Service
public class HospitalDeviceServiceImpl implements HospitalDeviceService {

//    @Autowired
//    private

    @Autowired
    private HospitalDeviceMapper hospitalDeviceMapper;

    @Autowired
    private UserLeaseOrderMapper userLeaseOrderMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private AgentMapper agentMapper;

    @Override
    public List<DeviceType> selectDeviceType(Long userId) {
        return hospitalDeviceMapper.selectDeviceType(userId);
    }

    @Override
    public DeviceStatisticsVo selectDeviceTypeDetails(Long deviceTypeId, Long userId, String deviceDepartment) {
        //根据当前账号搜索代理医院id
        SysUser user = sysUserMapper.selectUserById(userId);
        HospitalUser hospitalUser = hospitalDeviceMapper.selectHospitalbyUserName(user.getUserName());
        //搜索该设备数量及对应详细地址
        List<Device> deviceList = hospitalDeviceMapper.selectDeviceTypeDetails(deviceTypeId, hospitalUser.getHospitalId());
        List<DeviceDetailsVo> deviceDetailsVos = new ArrayList<>();
        DeviceStatisticsVo deviceStatisticsVo = new DeviceStatisticsVo();
        //遍历分割详细地址，赋值后返回数据
        deviceList.stream().forEach(map -> {
            Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(hospitalUser.getHospitalId());
            DeviceDetailsVo deviceDetailsVo = new DeviceDetailsVo();
            String device_full_address = map.getDeviceFullAddress();
            if (!device_full_address.isEmpty()) {
                String[] array = JSON.parseArray(device_full_address).toArray(new String[0]);
                for (int i = 0; i < array.length; i++) {
                    Hospital deviceFllor = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[0]));
                    Hospital Department = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[1]));
                    Hospital deviceRoom = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[2]));
                    Hospital deviceBed = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[3]));
                    deviceDetailsVo.setDeviceFloor(deviceFllor.getHospitalName());
                    deviceDetailsVo.setDeviceDepartment(Department.getHospitalName());
                    deviceDetailsVo.setDeviceRoom(deviceRoom.getHospitalName());
                    deviceDetailsVo.setDeviceBed(deviceBed.getHospitalName());
                }
                deviceDetailsVo.setDeviceFullAddress(device_full_address);
                deviceDetailsVo.setDeviceNumber(map.getDeviceNumber());
                deviceDetailsVo.setStatus(map.getStatus());
                deviceDetailsVo.setHospitalId(hospital.getHospitalId());
                deviceDetailsVo.setHospitalName(hospital.getHospitalName());
                deviceDetailsVos.add(deviceDetailsVo);
            }
        });
        if (deviceDepartment.equals("")){
            deviceDetailsVos.stream().forEach(map->{
                List<UserLeaseOrder> userLeaseOrder = userLeaseOrderMapper.selectUserLeaseOrderByDeviceNumber(map.getDeviceNumber());
                BigDecimal bigDecimal = new BigDecimal(0);
                userLeaseOrder.stream().forEach(i->{
                    BigDecimal decimal = new BigDecimal(i.getNetAmount());
                    bigDecimal.add(decimal);
                });
                deviceStatisticsVo.setDeviceAmount(bigDecimal);
            });
            deviceStatisticsVo.setDeviceDetailsVoList(deviceDetailsVos);
            return deviceStatisticsVo;
        }else {
            List<DeviceDetailsVo> collect = deviceDetailsVos.stream().filter(map -> map.getDeviceDepartment().equals(deviceDepartment)).collect(Collectors.toList());
            collect.stream().forEach(map->{
                List<UserLeaseOrder> userLeaseOrder = userLeaseOrderMapper.selectUserLeaseOrderByDeviceNumber(map.getDeviceNumber());
                BigDecimal bigDecimal = new BigDecimal(0);
                userLeaseOrder.stream().forEach(i->{
                    BigDecimal decimal = new BigDecimal(i.getNetAmount());
                    bigDecimal.add(decimal);
                });
                deviceStatisticsVo.setDeviceAmount(bigDecimal);
            });
            deviceStatisticsVo.setDeviceDetailsVoList(collect);
            return deviceStatisticsVo;
        }
    }

    @Override
    public void updateDeviceDetails(Long floorId,Long departmentId,Long roomId,Long bedId,String deviceNumber) {
        String floor = String.valueOf(floorId);
        String department = String.valueOf(departmentId);
        String room = String.valueOf(roomId);
        String bed = String.valueOf(bedId);
        List<String> fullAddress = new ArrayList<>();
        fullAddress.add(floor);
        fullAddress.add(department);
        fullAddress.add(room);
        fullAddress.add(bed);
        Gson gson = new Gson();
        String deviceFullAddress = gson.toJson(fullAddress);
        hospitalDeviceMapper.updateDeviceDetails(deviceNumber, deviceFullAddress);
    }

    @Override
    public List<GoodsOrderVo> selectGoodsOrder(String userId) {
        List<GoodsOrder> goodsOrders = hospitalDeviceMapper.selectGoodsOrder(Long.parseLong(String.valueOf(userId)));
        List<GoodsOrderVo> goodsOrderVos = new ArrayList<>();
        goodsOrders.stream().forEach(map -> {
            GoodsOrderVo orderVo = new GoodsOrderVo();
            Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(map.getHospitalId());
            DeviceType deviceType = hospitalDeviceMapper.selectDeviceByTypeName(map.getDeviceTypeId());
            Goods goods = hospitalDeviceMapper.selectGoodsByGoodsName(map.getGoodsId());
            BeanUtils.copyProperties(map, orderVo);
            orderVo.setHospitalName(hospital.getHospitalName());
            orderVo.setDeviceTypeName(deviceType.getDeviceTypeName());
            orderVo.setGoodsName(goods.getGoodsName());
            goodsOrderVos.add(orderVo);
        });
        return goodsOrderVos;
    }

    @Override
    public GoodsOrderVo selectOrderByOrderId(Long orderId) {
        GoodsOrder goodsOrder = hospitalDeviceMapper.selectOrderByOrderId(orderId);
        GoodsOrderVo goodsOrderVo = new GoodsOrderVo();
        Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(goodsOrder.getHospitalId());
        DeviceType deviceType = hospitalDeviceMapper.selectDeviceByTypeName(goodsOrder.getDeviceTypeId());
        Goods goods = hospitalDeviceMapper.selectGoodsByGoodsName(goodsOrder.getGoodsId());
        BeanUtils.copyProperties(hospital, goodsOrderVo);
        BeanUtils.copyProperties(deviceType, goodsOrderVo);
        BeanUtils.copyProperties(goods, goodsOrderVo);
        BeanUtils.copyProperties(goodsOrder, goodsOrderVo);
        return goodsOrderVo;
    }

    @Override
    public List<String> selectDepartment(Long userId) {
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        HospitalUser hospitalUser = hospitalDeviceMapper.selectHospitalbyUserName(sysUser.getUserName());
        List<Device> deviceList = hospitalDeviceMapper.selectDeviceByHospitalId(hospitalUser.getHospitalId());
        List<String> deviceDepartment = new ArrayList<>();
        deviceList.stream().forEach(map -> {
            String device_full_address = map.getDeviceFullAddress();
            if (!device_full_address.isEmpty()) {
                String[] array = JSON.parseArray(device_full_address).toArray(new String[0]);
                for (int i = 0; i < array.length; i++) {
                    Hospital Department = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[1]));
                    deviceDepartment.add(Department.getHospitalName());
                }
            }
        });
        List<String> collect = deviceDepartment.stream().distinct().collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<String> selectDeviceTypeName() {
        List<DeviceType> deviceTypes = hospitalDeviceMapper.selectDeviceTypeName();
        List<String> deviceType = new ArrayList<>();
        deviceTypes.stream().forEach(map->{
            deviceType.add(map.getDeviceTypeName());
        });
        return deviceType;
    }

    @Override
    public List<UserLeaseOrderVo> selectLeaseOrder(String userId,String deviceDepartment,String deviceTypeName,String orderNumber) {
        SysUser sysUser = sysUserMapper.selectUserById(Long.parseLong(String.valueOf(userId)));
        HospitalUser hospitalUser = hospitalDeviceMapper.selectHospitalbyUserName(sysUser.getUserName());
        List<String> numberList = hospitalDeviceMapper.selectLeaseOrder(hospitalUser.getHospitalId());
        List<UserLeaseOrder> userLeaseOrders = userLeaseOrderMapper.selectUserLeaseOrder(numberList);
        List<UserLeaseOrderVo> userLeaseOrderList = userLeaseOrders.stream().map(a -> {
            UserLeaseOrderVo b = new UserLeaseOrderVo();
            BeanUtils.copyProperties(a, b);
            return b;
        }).collect(Collectors.toList());
        userLeaseOrderList.stream().forEach(map->{
            Device device = hospitalDeviceMapper.selectDeviceByTypeNumber(map.getDeviceNumber());
            String deviceFullAddress = device.getDeviceFullAddress();
            if (!deviceFullAddress.isEmpty()) {
                String[] array = JSON.parseArray(deviceFullAddress).toArray(new String[0]);
                for (int i = 0; i < array.length; i++) {
                    Hospital department = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[1]));
                    map.setDepartment(department.getHospitalName());
                }
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
        }if (!orderNumber.equals("")){
            List<UserLeaseOrderVo> collect = userLeaseOrderList.stream().filter(map -> map.getOrderNumber().equals(orderNumber)).collect(Collectors.toList());
            userLeaseOrderList.clear();
            userLeaseOrderList.addAll(collect);
        }
        return userLeaseOrderList;
    }

    @Override
    public UserLeaseOrderVo selectLeaseOrderDetails(String orderNumber) {
        UserLeaseOrder userLeaseOrder = userLeaseOrderMapper.selectLeaseOrderDetails(orderNumber);
        SysUser sysUser = sysUserMapper.selectUserByOpenid(userLeaseOrder.getOpenid());
        HospitalUser hospitalUser = hospitalDeviceMapper.selectHospitalbyUserName(sysUser.getUserName());
        //获取医院信息
        Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(hospitalUser.getHospitalId());
        //获取代理商信息
        AgentHospital agentHospital = agentMapper.selectAgentByHospitalId(hospitalUser.getHospitalId());
        //获取设备类型押金
        DeviceType devicetype = hospitalDeviceMapper.selectDeviceByType(userLeaseOrder.getDeviceType());
        Agent agent = agentMapper.selectAgent(agentHospital.getAgentId());
        UserLeaseOrderVo userLeaseOrderVo = new UserLeaseOrderVo();
        BeanUtils.copyProperties(userLeaseOrder, userLeaseOrderVo);
        userLeaseOrderVo.setNickName(sysUser.getNickName());
        userLeaseOrderVo.setProportion(sysUser.getProportion());
        userLeaseOrderVo.setAgentName(agent.getAgentName());
        userLeaseOrderVo.setHospitalName(hospital.getHospitalName());
        BigDecimal deviceTypeDeposit = new BigDecimal(String.valueOf(devicetype.getDeviceTypeDeposit()));
        userLeaseOrderVo.setDepositNum(deviceTypeDeposit);
        return userLeaseOrderVo;
    }

    @Override
    public TotalVo selectRevenueStatistics(String userName, int statistics) {
        HospitalUser hospitalUser = hospitalDeviceMapper.selectHospitalbyUserName(userName);
        List<String> deviceNumberList = hospitalDeviceMapper.selectLeaseOrder(hospitalUser.getHospitalId());
        SysUser sysUser = sysUserMapper.selectUserByUserName(userName);
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
                BigDecimal totalFee = new BigDecimal(sysUser.getProportion()/100);
                orderVo.setIncomeAmount(decimal.multiply(totalFee));
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
                BigDecimal totalFee = new BigDecimal(sysUser.getProportion()/100);
                orderVo.setIncomeAmount(decimal.multiply(totalFee));
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
                BigDecimal totalFee = new BigDecimal(sysUser.getProportion()/100);
                orderVo.setIncomeAmount(decimal.multiply(totalFee));
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
                BigDecimal totalFee = new BigDecimal(sysUser.getProportion()/100);
                orderVo.setIncomeAmount(decimal.multiply(totalFee));
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
    public AjaxResult loginHospitalPort(String userName, String password) {
        SysUser user = sysUserMapper.selectUserByUser(userName);
        if (user == null) {
            return AjaxResult.error("该账号不存在");
        }
        boolean matchesPassword = SecurityUtils.matchesPassword(password, user.getPassword());
        if (matchesPassword) {
            // 用户验证
            if ("1".equals(user.getStatus())) {
                return AjaxResult.error("账号已被封禁");
            }
            SysUser sysUser = new SysUser();
            sysUser.setUserId(user.getUserId());
            sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
            sysUser.setLoginDate(DateUtils.getNowDate());
            sysUserService.updateUserProfile(sysUser);
            return AjaxResult.success(user);
        }
        return AjaxResult.error("密码不正确，请重新登录");
    }

    @Override
    public List<Map<String,Object>> selectDeviceAddress(Long hospitalId) {
        List<Map<String,Object>> mapList = new ArrayList<>();
        List<Hospital> hospitals = hospitalDeviceMapper.selectHospitalByParentId(hospitalId);
        List<HospitalVo> hospitalVos = hospitals.stream().map(a -> {HospitalVo d = new HospitalVo();BeanUtils.copyProperties(a, d);return d;}).collect(Collectors.toList());
        hospitalVos.stream().forEach(map->{
            Map<String,Object> Map1 = new HashMap<>();
            Map1.put("floor",map);
            mapList.add(Map1);
            List<Map<String,Object>> mapList1 = new ArrayList<>();
            List<Hospital> hospitals1 = hospitalDeviceMapper.selectHospitalByParentId(map.getHospitalId());
            List<HospitalVo> hospitalVos1 = hospitals1.stream().map(a -> {HospitalVo d = new HospitalVo();BeanUtils.copyProperties(a, d);return d;}).collect(Collectors.toList());
            hospitalVos1.stream().forEach(map1->{
                Map<String,Object> Map2 = new HashMap<>();
                Map2.put("department",map1);
                mapList1.add(Map2);
                List<Map<String,Object>> mapList2 = new ArrayList<>();
                List<Hospital> hospitals2 = hospitalDeviceMapper.selectHospitalByParentId(map1.getHospitalId());
                List<HospitalVo> hospitalVos2 = hospitals2.stream().map(a -> {HospitalVo d = new HospitalVo();BeanUtils.copyProperties(a, d);return d;}).collect(Collectors.toList());
                hospitalVos2.stream().forEach(map2-> {
                    Map<String, Object> Map3 = new HashMap<>();
                    Map3.put("room", map2);
                    mapList2.add(Map3);
                    List<Map<String, Object>> mapList3 = new ArrayList<>();
                    List<Hospital> hospitals3 = hospitalDeviceMapper.selectHospitalByParentId(map2.getHospitalId());
                    List<HospitalVo> hospitalVos3 = hospitals3.stream().map(a -> {HospitalVo d = new HospitalVo();BeanUtils.copyProperties(a, d);return d;}).collect(Collectors.toList());
                    hospitalVos3.stream().forEach(map3->{
                        Map<String, Object> Map4 = new HashMap<>();
                        Map4.put("bed", map3);
                        mapList3.add(Map4);
                    });
                    Map3.put("FOUR",mapList3);
                });
                Map2.put("THREE",mapList2);
            });
            Map1.put("TWO",mapList1);
        });
        return mapList;
    }

/*    @Override
    public Map<String,List<Object>> selectDeviceAddress1(Long hospitalId) {
        Map<String,List<Object>> listMap = new HashMap<>();
        List<Hospital> hospitals = hospitalDeviceMapper.selectHospitalByParentId(hospitalId);
        List<HospitalVo> hospitalVos = hospitals.stream().map(a -> {HospitalVo d = new HospitalVo();BeanUtils.copyProperties(a, d);return d;}).collect(Collectors.toList());
        Map<String, Object> one = new HashMap<>();
        one.put("hospital_four",hospitalVos);
        hospitalVos.stream().forEach(map->{
            List<Hospital> hospitals1 = hospitalDeviceMapper.selectHospitalByParentId(map.getHospitalId());
            List<HospitalVo> hospitalVos1 = hospitals1.stream().map(a -> {HospitalVo d = new HospitalVo();BeanUtils.copyProperties(a, d);return d;}).collect(Collectors.toList());
            Map<String, Object> two = new HashMap<>();
            two.put("hospital_two",hospitalVos1);
        });
        return listMap;
    }*/

}
