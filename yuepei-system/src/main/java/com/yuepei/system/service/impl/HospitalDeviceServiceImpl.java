package com.yuepei.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.google.gson.Gson;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.utils.DateUtils;
import com.yuepei.common.utils.SecurityUtils;
import com.yuepei.common.utils.ServletUtils;
import com.yuepei.common.utils.ip.IpUtils;
import com.yuepei.system.domain.*;
import com.yuepei.system.domain.vo.*;
import com.yuepei.system.mapper.*;
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
    private DeviceTypeMapper deviceTypeMapper;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private AgentMapper agentMapper;

    @Override
    public List<DeviceType> selectDeviceType(Long userId) {
        List<DeviceType> deviceTypes = new ArrayList<>();
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(sysUser.getHospitalId());
        List<Device> deviceList = hospitalDeviceMapper.selectDeviceByHospitalId(hospital.getHospitalId());
        deviceList.stream().forEach(map->{
            DeviceType deviceType = deviceTypeMapper.selectDeviceTypeByDeviceTypeId(map.getDeviceTypeId());
            deviceTypes.add(deviceType);
        });
        List<DeviceType> collect = deviceTypes.stream().distinct().collect(Collectors.toList());
        return collect;
    }

    @Override
    public DeviceStatisticsVo selectDeviceTypeDetails(Long deviceTypeId, Long userId, String deviceDepartment) {
        //根据当前账号搜索代理医院id
        SysUser user = sysUserMapper.selectUserById(userId);
        Hospital hospitalName = hospitalDeviceMapper.selectHospitalByHospitalName(user.getHospitalId());
        //搜索该设备数量及对应详细地址
        List<Device> deviceList = hospitalDeviceMapper.selectDeviceTypeDetails(deviceTypeId, user.getHospitalId());
        List<DeviceDetailsVo> deviceDetailsVos = new ArrayList<>();
        DeviceStatisticsVo deviceStatisticsVo = new DeviceStatisticsVo();
        //遍历分割详细地址，赋值后返回数据
        deviceList.stream().forEach(map -> {
            Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(user.getHospitalId());
            DeviceDetailsVo deviceDetailsVo = new DeviceDetailsVo();
            String device_full_address = map.getDeviceFullAddress();
            if (!device_full_address.equals("0")) {
                String[] array = JSON.parseArray(device_full_address).toArray(new String[0]);
                Hospital deviceFloor = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[0]));
                Hospital Department = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[1]));
                Hospital deviceRoom = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[2]));
                Hospital deviceBed = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[3]));
                deviceDetailsVo.setDeviceFloor(deviceFloor.getHospitalId());
                deviceDetailsVo.setDeviceDepartment(Department.getHospitalId());
                deviceDetailsVo.setDeviceDepartmentName(Department.getHospitalName());
                deviceDetailsVo.setDeviceRoom(deviceRoom.getHospitalId());
                deviceDetailsVo.setDeviceBed(deviceBed.getHospitalId());
                deviceDetailsVo.setDeviceFullAddress(device_full_address);
                deviceDetailsVo.setDeviceNumber(map.getDeviceNumber());
                deviceDetailsVo.setStatus(map.getStatus());
                deviceDetailsVo.setHospitalId(hospital.getHospitalId());
                deviceDetailsVo.setHospitalName(hospital.getHospitalName());
                deviceDetailsVos.add(deviceDetailsVo);
            }else {
                deviceDetailsVo.setDeviceNumber(map.getDeviceNumber());
                deviceDetailsVo.setStatus(map.getStatus());
                deviceDetailsVo.setHospitalId(hospital.getHospitalId());
                deviceDetailsVo.setHospitalName(hospital.getHospitalName());
                deviceDetailsVos.add(deviceDetailsVo);
            }
        });
        List<UserLeaseOrderVo> leaseOrderVos = new ArrayList<>();
        List<String> deviceNumbers = new ArrayList<>();
        deviceDetailsVos.stream().forEach(map->{
            deviceNumbers.add(map.getDeviceNumber());
        });
        if (deviceNumbers.size()!=0){
            List<UserLeaseOrder> userLeaseOrders = hospitalDeviceMapper.selectUserLeaseOrderByDevices(deviceNumbers,String.valueOf(user.getHospitalId()));
            userLeaseOrders.stream().forEach(i->{
                UserLeaseOrderVo userLeaseOrderVo = new UserLeaseOrderVo();
                BeanUtils.copyProperties(i,userLeaseOrderVo);
                leaseOrderVos.add(userLeaseOrderVo);
            });
            BigDecimal reduce = leaseOrderVos.stream().map(UserLeaseOrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            deviceStatisticsVo.setDeviceAmount(reduce);
        }
        if (!deviceDepartment.equals("")){
            List<DeviceDetailsVo> collect = new ArrayList<>();
            List<DeviceDetailsVo> deviceDetailsVos1 = deviceDetailsVos.stream()
                    .filter(map -> map.getDeviceDepartmentName()!=null)
                    .filter(i -> i.getDeviceDepartmentName().equals(deviceDepartment))
                    .collect(Collectors.toList());
            collect.addAll(deviceDetailsVos1);
            deviceDetailsVos.clear();
            deviceDetailsVos.addAll(collect);
            List<UserLeaseOrderVo> userLeaseOrderVos = new ArrayList<>();
            List<String> deviceNumberList = new ArrayList<>();
            deviceDetailsVos.stream().forEach(map->{
                deviceNumberList.add(map.getDeviceNumber());
            });
            List<UserLeaseOrder> userLeaseOrder = hospitalDeviceMapper.selectUserLeaseOrderByDevices(deviceNumberList, String.valueOf(hospitalName.getHospitalId()));
            userLeaseOrder.stream().forEach(i->{
                UserLeaseOrderVo userLeaseOrderVo = new UserLeaseOrderVo();
                BeanUtils.copyProperties(i,userLeaseOrderVo);
                userLeaseOrderVo.setNetAmount(i.getNetAmount());
                userLeaseOrderVos.add(userLeaseOrderVo);
            });
            BigDecimal bigDecimal = userLeaseOrderVos.stream().map(UserLeaseOrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            deviceStatisticsVo.setDeviceAmount(bigDecimal);
        }
        deviceStatisticsVo.setHospitalId(hospitalName.getHospitalId());
        deviceStatisticsVo.setHospitalName(hospitalName.getHospitalName());
        deviceStatisticsVo.setDeviceDetailsVoList(deviceDetailsVos);
        return deviceStatisticsVo;
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
    public List<GoodsOrderVo> selectGoodsOrder(Long userId) {
        List<GoodsOrder> goodsOrders = hospitalDeviceMapper.selectGoodsOrder(userId);
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
        List<Device> deviceList = hospitalDeviceMapper.selectDeviceByHospitalId(sysUser.getHospitalId());
        List<String> deviceDepartment = new ArrayList<>();
        deviceList.stream().forEach(map -> {
            String device_full_address = map.getDeviceFullAddress();
            if (!device_full_address.equals("0")) {
                String[] array = JSON.parseArray(device_full_address).toArray(new String[0]);
                Hospital Department = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[1]));
                deviceDepartment.add(Department.getHospitalName());
            }
        });
        List<String> collect = deviceDepartment.stream().distinct().collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<String> selectDeviceTypeName(Long userId) {
        List<String> deviceType = new ArrayList<>();
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        List<Device> deviceList = hospitalDeviceMapper.selectDeviceByHospitalId(sysUser.getHospitalId());
        List<Long> deviceTypeId = new ArrayList<>();
        deviceList.stream().forEach(map->{
            deviceTypeId.add(map.getDeviceTypeId());
        });
        List<Long> collect = deviceTypeId.stream().distinct().collect(Collectors.toList());
        collect.stream().forEach(map->{
            DeviceType type = deviceTypeMapper.selectDeviceTypeByDeviceTypeId(map);
            deviceType.add(type.getDeviceTypeName());
        });
        return deviceType;
    }

    @Override
    public List<UserLeaseOrderVo> selectLeaseOrder(Long userId,String deviceDepartment,String deviceTypeName,String orderNumber) {
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        List<String> numberList = new ArrayList<>();
        List<Device> devices = hospitalDeviceMapper.selectDeviceByHospitalId(sysUser.getHospitalId());
        devices.stream().forEach(map->{
            numberList.add(map.getDeviceNumber());
        });
        List<UserLeaseOrder> leaseOrders = new ArrayList<>();
        if (!orderNumber.equals("")){
            List<UserLeaseOrder> userLeaseOrders = userLeaseOrderMapper.selectUserLeaseOrderByOrderNumber(orderNumber, String.valueOf(sysUser.getHospitalId()));
            numberList.stream().forEach(map->{
                List<UserLeaseOrder> collect = userLeaseOrders.stream().filter(i -> i.getDeviceNumber().equals(map)).collect(Collectors.toList());
                leaseOrders.addAll(collect);
            });
        }else {
            if (numberList.size()!=0){
                List<UserLeaseOrder> userLeaseOrders = userLeaseOrderMapper.selectUserLeaseOrderByHospitalIdAndStatus(String.valueOf(sysUser.getHospitalId()));
                leaseOrders.addAll(userLeaseOrders);
            }
        }
        List<UserLeaseOrderVo> userLeaseOrderList = leaseOrders.stream().map(a -> {
            UserLeaseOrderVo b = new UserLeaseOrderVo();
            BeanUtils.copyProperties(a, b);
            return b;
        }).collect(Collectors.toList());
        if (leaseOrders.size()==0){
            return userLeaseOrderList;
        }
        userLeaseOrderList.stream().forEach(map->{
            Device device = hospitalDeviceMapper.selectDeviceByTypeNumber(map.getDeviceNumber());
            String deviceFullAddress = device.getDeviceFullAddress();
            if (!deviceFullAddress.equals("0")) {
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
        List<UserLeaseOrderVo> collect = userLeaseOrderList.stream().filter(map -> !map.getStatus().equals("3")).collect(Collectors.toList());
        return collect;
    }

    @Override
    public UserLeaseOrderVo selectLeaseOrderDetails(String orderNumber, Long userId) {
        SysUser sysUser1 = sysUserMapper.selectUserById(userId);
        UserLeaseOrder userLeaseOrder = userLeaseOrderMapper.selectLeaseOrderDetails(orderNumber);
        Device device = hospitalDeviceMapper.selectDeviceByTypeNumber(userLeaseOrder.getDeviceNumber());
        //获取医院信息
        Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(device.getHospitalId());
        //获取代理商信息
        UserLeaseOrderVo userLeaseOrderVo = new UserLeaseOrderVo();
        BeanUtils.copyProperties(userLeaseOrder, userLeaseOrderVo);
        userLeaseOrderVo.setUserName(sysUser1.getUserName());
        userLeaseOrderVo.setProportion(sysUser1.getProportion());
        userLeaseOrderVo.setAgentName(sysUser1.getNickName());
        if (hospital!=null){
            userLeaseOrderVo.setHospitalName(hospital.getHospitalName());
        }
        if (userLeaseOrder.getStatus().equals("0")){
            Long date = new Date().getTime();
            Long lease = userLeaseOrderVo.getLeaseTime().getTime();
            //已使用时长、使用时长
            Long time = new Date(date - lease).getTime();
            Long day = time/1000/60/60/24;
            Long hour = time/1000/60/60%24;
            Long minute = time/1000/60%60;
            Long second = time/1000%60;
            if (day==0){
                if (hour==0){
                    if (minute==0){
                        userLeaseOrderVo.setPlayTime(second+"秒");
                    }else {
                        userLeaseOrderVo.setPlayTime(minute+"分钟"+second+"秒");
                    }
                }else {
                    userLeaseOrderVo.setPlayTime(hour+"小时"+minute+"分钟"+second+"秒");
                }
            }else {
                userLeaseOrderVo.setPlayTime(day+"天"+hour+"小时"+minute+"分钟"+second+"秒");
            }
            Date leaseTime = userLeaseOrderVo.getLeaseTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat Format = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
            Date date1 = new Date();
            String leaseTime1 = Format.format(leaseTime);
            String newDate = Format.format(date1);
            String format = dateFormat.format(leaseTime);
            String format1 = dateFormat.format(date1);
            JSONArray deviceRule = JSON.parseArray(userLeaseOrder.getDeviceRule());
            BigDecimal price1 = BigDecimal.ZERO;
            for (int i = 0; i < deviceRule.size(); i++) {
                JSONObject jsonObject = deviceRule.getJSONObject(i);
                Integer time1 = (Integer) jsonObject.get("time");
                String startTime = (String) jsonObject.get("startTime");
                try {
                    Date parse = dateFormat.parse(format);
                    Date start = dateFormat.parse(startTime);
                    Date now = dateFormat.parse(format1);
                    Date parse1 = Format.parse(leaseTime1);
                    Date parse2 = Format.parse(newDate);
                    if (start.compareTo(parse)==1){
                        if (time1==0){
                            Long time2 = new Date(parse2.getTime() - parse1.getTime()).getTime();
                            Long minute1 = time2 / 1000 / 60 / 60;
                            Long minute2 = time2 / 1000 / 60 % 60 ;
                            Object price = jsonObject.get("price");
                            BigDecimal bigDecimal = new BigDecimal(String.valueOf(price));
                            userLeaseOrderVo.setContent(bigDecimal+device.getContent());
                            price1=price1.add(bigDecimal);
                            if (minute2<10){
                                userLeaseOrderVo.setEvaluate(bigDecimal.multiply(new BigDecimal(minute1)));
                            }else {
                                userLeaseOrderVo.setEvaluate(bigDecimal.multiply(new BigDecimal(minute1+1)));
                            }
                        }else if (time1==1){
                            if (now.compareTo(start)==1){
                                Long time2 = new Date(start.getTime() - parse.getTime()).getTime();
                                Long minute1 = time2 / 1000 / 60 / 60;
                                Long minute2 = time2 / 1000 / 60 % 60 ;
                                Object price = jsonObject.get("price");
                                BigDecimal bigDecimal = new BigDecimal(String.valueOf(price));
                                userLeaseOrderVo.setContent(bigDecimal+device.getContent());
                                if (minute2<10){
                                    userLeaseOrderVo.setEvaluate(price1.multiply(new BigDecimal(minute1)).add(bigDecimal));
                                }else {
                                    userLeaseOrderVo.setEvaluate(price1.multiply(new BigDecimal(minute1+1)).add(bigDecimal));
                                }
                            }
                        }
                    }else {
                        if (time1==0){
                            Long time2 = new Date(parse2.getTime() - parse1.getTime()).getTime();
                            Long minute1 = time2 / 1000 / 60 /60 ;
                            Long minute2 = time2 / 1000 / 60 % 60 ;
                            Object price = jsonObject.get("price");
                            BigDecimal bigDecimal = new BigDecimal(String.valueOf(price));
                            userLeaseOrderVo.setContent(bigDecimal+device.getContent());
                            price1=price1.add(bigDecimal);
                            if (minute2<10){
                                userLeaseOrderVo.setEvaluate(bigDecimal.multiply(new BigDecimal(minute1)));
                            }else {
                                userLeaseOrderVo.setEvaluate(bigDecimal.multiply(new BigDecimal(minute1+1)));
                            }
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            userLeaseOrderVo.setDepositNum(new BigDecimal(userLeaseOrder.getDeposit()));
            userLeaseOrderVo.setLeaseTime(userLeaseOrder.getLeaseTime());
            userLeaseOrderVo.setLeaseAddress(userLeaseOrder.getLeaseAddress());
            userLeaseOrderVo.setOrderNumber(userLeaseOrder.getOrderNumber());
        }
        else if (userLeaseOrder.getStatus().equals("1")){
            Long time = Long.valueOf(userLeaseOrderVo.getPlayTime());
            Long day = time/1000/60/60/24;
            Long hour = time/1000/60/60%24;
            Long minute = time/1000/60%60;
            Long second = time/1000%60;
            if (day==0){
                if (hour==0){
                    if (minute==0){
                        userLeaseOrderVo.setPlayTime(second+"秒");
                    }else {
                        userLeaseOrderVo.setPlayTime(minute+"分钟"+second+"秒");
                    }
                }else {
                    userLeaseOrderVo.setPlayTime(hour+"小时"+minute+"分钟"+second+"秒");
                }
            }else {
                userLeaseOrderVo.setPlayTime(day+"天"+hour+"小时"+minute+"分钟"+second+"秒");
            }
            userLeaseOrderVo.setPrice(userLeaseOrder.getPrice());
            JSONArray deviceRule = JSON.parseArray(userLeaseOrder.getDeviceRule());
            for (int i = 0; i < deviceRule.size(); i++) {
                JSONObject jsonObject = deviceRule.getJSONObject(i);
                Integer time1 = (Integer) jsonObject.get("time");
                if (time1==0){
                    Object price = jsonObject.get("price");
                    BigDecimal bigDecimal = new BigDecimal(String.valueOf(price));
                    userLeaseOrderVo.setContent(bigDecimal+device.getContent());
                }else {
                    String start = String.valueOf(jsonObject.get("startTime"));
                    String end = String.valueOf(jsonObject.get("endTime"));
                    Object price = jsonObject.get("price");
                    BigDecimal bigDecimal = new BigDecimal(String.valueOf(price));
                    userLeaseOrderVo.setEstimateAmount("("+start+"~"+end+")  "+bigDecimal);
                }
            }
            userLeaseOrderVo.setDepositNum(new BigDecimal(userLeaseOrder.getDeposit()));
            userLeaseOrderVo.setCouponPrice(userLeaseOrder.getCouponPrice());
            userLeaseOrderVo.setLeaseTime(userLeaseOrder.getLeaseTime());
            userLeaseOrderVo.setLeaseAddress(userLeaseOrder.getLeaseAddress());
            userLeaseOrderVo.setRestoreTime(userLeaseOrder.getRestoreTime());
            userLeaseOrderVo.setRestoreAddress(userLeaseOrder.getRestoreAddress());
            userLeaseOrderVo.setOrderNumber(userLeaseOrder.getOrderNumber());
            userLeaseOrderVo.setStatus(userLeaseOrder.getStatus());
            userLeaseOrderVo.setNetAmount(userLeaseOrder.getNetAmount());
        }
        else {
            Long time = Long.valueOf(userLeaseOrderVo.getPlayTime());
            Long day = time/1000/60/60/24;
            Long hour = time/1000/60/60%24;
            Long minute = time/1000/60%60;
            Long second = time/1000%60;
            if (day==0){
                if (hour==0){
                    if (minute==0){
                        userLeaseOrderVo.setPlayTime(second+"秒");
                    }else {
                        userLeaseOrderVo.setPlayTime(minute+"分钟"+second+"秒");
                    }
                }else {
                    userLeaseOrderVo.setPlayTime(hour+"小时"+minute+"分钟"+second+"秒");
                }
            }else {
                userLeaseOrderVo.setPlayTime(day+"天"+hour+"小时"+minute+"分钟"+second+"秒");
            }
            userLeaseOrderVo.setPrice(userLeaseOrder.getPrice());
            JSONArray deviceRule = JSON.parseArray(userLeaseOrder.getDeviceRule());
            for (int i = 0; i < deviceRule.size(); i++) {
                JSONObject jsonObject = deviceRule.getJSONObject(i);
                Integer time1 = (Integer) jsonObject.get("time");
                if (time1==0){
                    Object price = jsonObject.get("price");
                    BigDecimal bigDecimal = new BigDecimal(String.valueOf(price));
                    userLeaseOrderVo.setContent(bigDecimal+device.getContent());
                }else {
                    String start = String.valueOf(jsonObject.get("startTime"));
                    String end = String.valueOf(jsonObject.get("endTime"));
                    Object price = jsonObject.get("price");
                    BigDecimal bigDecimal = new BigDecimal(String.valueOf(price));
                    userLeaseOrderVo.setEstimateAmount("("+start+"~"+end+")  "+bigDecimal);
                }
            }
            userLeaseOrderVo.setDepositNum(new BigDecimal(userLeaseOrder.getDeposit()));
            userLeaseOrderVo.setCouponPrice(userLeaseOrder.getCouponPrice());
            userLeaseOrderVo.setLeaseTime(userLeaseOrder.getLeaseTime());
            userLeaseOrderVo.setLeaseAddress(userLeaseOrder.getLeaseAddress());
            userLeaseOrderVo.setRestoreTime(userLeaseOrder.getRestoreTime());
            userLeaseOrderVo.setRestoreAddress(userLeaseOrder.getRestoreAddress());
            userLeaseOrderVo.setOrderNumber(userLeaseOrder.getOrderNumber());
            userLeaseOrderVo.setPayType(userLeaseOrder.getPayType());
            userLeaseOrderVo.setStatus(userLeaseOrder.getStatus());
            userLeaseOrderVo.setNetAmount(userLeaseOrder.getNetAmount());
        }
        return userLeaseOrderVo;
    }

    @Override
    public TotalVo selectRevenueStatistics(String userName, int statistics) {
        SysUser sysUser = sysUserMapper.selectUserByUserName(userName);
        TotalVo totalVo = new TotalVo();
        List<OrderVo> orderVos = new ArrayList<>();
        Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(sysUser.getHospitalId());
        if (statistics == 1) {
            List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectUserLeaseOrderByHospitalId(String.valueOf(hospital.getHospitalId()));
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
                BigDecimal decimal = map.getNetAmount();
                orderVo.setNetAmount(decimal);
                orderVo.setDividendRatio(map.getHospitalProportion());
                orderVo.setIncomeAmount(decimal.multiply(new BigDecimal(map.getHospitalProportion())).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
                orderVos.add(orderVo);
            });
            totalVo.setEffectiveOrder(orderVos.size());
            BigDecimal orderAmount = orderVos.stream().map(OrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal dividendAmount = orderVos.stream().map(OrderVo::getIncomeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            totalVo.setOrderAmount(orderAmount);
            totalVo.setDividendAmount(dividendAmount);
            totalVo.setOrderVos(orderVos);
        } else if (statistics == 2) {
            List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectUserLeaseOrderByHospitalId(String.valueOf(hospital.getHospitalId()));
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
                BigDecimal decimal = map.getNetAmount();
                orderVo.setNetAmount(decimal);
                orderVo.setDividendRatio(map.getHospitalProportion());
                orderVo.setIncomeAmount(decimal.multiply(new BigDecimal(map.getHospitalProportion())).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
                orderVos.add(orderVo);
            });
            totalVo.setEffectiveOrder(orderVos.size());
            BigDecimal orderAmount = orderVos.stream().map(OrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal dividendAmount = orderVos.stream().map(OrderVo::getIncomeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            totalVo.setOrderAmount(orderAmount);
            totalVo.setDividendAmount(dividendAmount);
            totalVo.setOrderVos(orderVos);
        } else if (statistics == 3) {
            List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectUserLeaseOrderByHospitalId(String.valueOf(hospital.getHospitalId()));
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
                BigDecimal decimal = map.getNetAmount();
                orderVo.setNetAmount(decimal);
                orderVo.setDividendRatio(map.getHospitalProportion());
                orderVo.setIncomeAmount(decimal.multiply(new BigDecimal(map.getHospitalProportion())).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
                orderVos.add(orderVo);
            });
            totalVo.setEffectiveOrder(orderVos.size());
            BigDecimal orderAmount = orderVos.stream().map(OrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal dividendAmount = orderVos.stream().map(OrderVo::getIncomeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            totalVo.setOrderAmount(orderAmount);
            totalVo.setDividendAmount(dividendAmount);
            totalVo.setOrderVos(orderVos);
        } else {
            List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectUserLeaseOrderByHospitalId(String.valueOf(hospital.getHospitalId()));
            userLeaseOrderList.stream().forEach(map->{
                OrderVo orderVo = new OrderVo();
                orderVo.setOrderNumber(map.getOrderNumber());
                BigDecimal decimal = map.getNetAmount();
                orderVo.setNetAmount(decimal);
                orderVo.setDividendRatio(map.getHospitalProportion());
                orderVo.setIncomeAmount(decimal.multiply(new BigDecimal(map.getHospitalProportion())).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
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
        SysUser user = sysUserMapper.selectUserByUserName(userName);
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
        List<HospitalVO> hospitalVos = hospitals.stream().map(a -> {HospitalVO d = new HospitalVO();BeanUtils.copyProperties(a, d);return d;}).collect(Collectors.toList());
        hospitalVos.stream().forEach(map->{
            Map<String,Object> Map1 = new HashMap<>();
            Map1.put("floor",map);
            mapList.add(Map1);
            List<Map<String,Object>> mapList1 = new ArrayList<>();
            List<Hospital> hospitals1 = hospitalDeviceMapper.selectHospitalByParentId(map.getHospitalId());
            List<HospitalVO> hospitalVos1 = hospitals1.stream().map(a -> {HospitalVO d = new HospitalVO();BeanUtils.copyProperties(a, d);return d;}).collect(Collectors.toList());
            hospitalVos1.stream().forEach(map1->{
                Map<String,Object> Map2 = new HashMap<>();
                Map2.put("department",map1);
                mapList1.add(Map2);
                List<Map<String,Object>> mapList2 = new ArrayList<>();
                List<Hospital> hospitals2 = hospitalDeviceMapper.selectHospitalByParentId(map1.getHospitalId());
                List<HospitalVO> hospitalVos2 = hospitals2.stream().map(a -> {HospitalVO d = new HospitalVO();BeanUtils.copyProperties(a, d);return d;}).collect(Collectors.toList());
                hospitalVos2.stream().forEach(map2-> {
                    Map<String, Object> Map3 = new HashMap<>();
                    Map3.put("room", map2);
                    mapList2.add(Map3);
                    List<Map<String, Object>> mapList3 = new ArrayList<>();
                    List<Hospital> hospitals3 = hospitalDeviceMapper.selectHospitalByParentId(map2.getHospitalId());
                    List<HospitalVO> hospitalVos3 = hospitals3.stream().map(a -> {HospitalVO d = new HospitalVO();BeanUtils.copyProperties(a, d);return d;}).collect(Collectors.toList());
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

    @Override
    public Map<String,List<HospitalVO>> selectDeviceAddress1(Long hospitalId) {
        Map<String,List<HospitalVO>> listMap = new HashMap<>();
        List<Hospital> hospitals = hospitalDeviceMapper.selectHospitalByParentId(hospitalId);
        List<HospitalVO> hospitalOne = hospitals.stream().map(a -> {HospitalVO d = new HospitalVO();BeanUtils.copyProperties(a, d);return d;}).collect(Collectors.toList());
        List<HospitalVO> hospitalTwo = new ArrayList<>();
        List<HospitalVO> hospitalThree = new ArrayList<>();
        List<HospitalVO> hospitalFour = new ArrayList<>();
        hospitalOne.stream().forEach(map->{
            List<Hospital> hospital = hospitalDeviceMapper.selectHospitalByParentId(map.getHospitalId());
            List<HospitalVO> hospitalVo = hospital.stream().map(a -> {HospitalVO d = new HospitalVO();BeanUtils.copyProperties(a, d);return d;}).collect(Collectors.toList());
            hospitalTwo.addAll(hospitalVo);
        });
        hospitalTwo.stream().forEach(map->{
            List<Hospital> hospital = hospitalDeviceMapper.selectHospitalByParentId(map.getHospitalId());
            List<HospitalVO> hospitalVo = hospital.stream().map(a -> {HospitalVO d = new HospitalVO();BeanUtils.copyProperties(a, d);return d;}).collect(Collectors.toList());
            hospitalThree.addAll(hospitalVo);
        });
        hospitalThree.stream().forEach(map->{
            List<Hospital> hospital = hospitalDeviceMapper.selectHospitalByParentId(map.getHospitalId());
            List<HospitalVO> hospitalVo = hospital.stream().map(a -> {HospitalVO d = new HospitalVO();BeanUtils.copyProperties(a, d);return d;}).collect(Collectors.toList());
            hospitalFour.addAll(hospitalVo);
        });
        listMap.put("hospital_one",hospitalOne);
        listMap.put("hospital_two",hospitalTwo);
        listMap.put("hospital_three",hospitalThree);
        listMap.put("hospital_four",hospitalFour);
        return listMap;
    }

    @Override
    public IndexVo indexPage(Long userId) {
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        IndexVo indexVo = new IndexVo();
        Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(sysUser.getHospitalId());
        List<UserLeaseOrderVo> userLeaseOrderVos = new ArrayList<>();
        List<UserLeaseOrder> userLeaseOrder = hospitalDeviceMapper.selectUserLeaseOrderByHospitalId(String.valueOf(sysUser.getHospitalId()));
        userLeaseOrder.stream().forEach(i->{
            UserLeaseOrderVo userLeaseOrderVo = new UserLeaseOrderVo();
            BeanUtils.copyProperties(i,userLeaseOrderVo);
            userLeaseOrderVo.setNetAmount(i.getNetAmount());
            userLeaseOrderVos.add(userLeaseOrderVo);
        });
        BigDecimal decimal = userLeaseOrderVos.stream().map(UserLeaseOrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        List<DeviceType> deviceTypes = new ArrayList<>();
        List<Device> deviceList = hospitalDeviceMapper.selectDeviceByHospitalId(sysUser.getHospitalId());
        deviceList.stream().forEach(map->{
            DeviceType deviceType = deviceTypeMapper.selectDeviceTypeByDeviceTypeId(map.getDeviceTypeId());
            deviceTypes.add(deviceType);
        });
        indexVo.setDeviceAmount(decimal);
        indexVo.setHospitalName(hospital.getHospitalName());
        indexVo.setSum(deviceList.size());
        indexVo.setProportion(sysUser.getProportion());
        return indexVo;
    }


    @Override
    public SysUser selectPersonalData(Long userId) {
        return sysUserMapper.selectUserById(userId);
    }

    @Override
    public PersonalCenterVo selectPersonalCenter(Long userId) {
        PersonalCenterVo personalCenterVo = new PersonalCenterVo();
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        personalCenterVo.setAmount(new BigDecimal(String.valueOf(sysUser.getBalance())));
        personalCenterVo.setUserName(sysUser.getUserName());
        personalCenterVo.setAvatar(sysUser.getAvatar());
        return personalCenterVo;
    }

}
