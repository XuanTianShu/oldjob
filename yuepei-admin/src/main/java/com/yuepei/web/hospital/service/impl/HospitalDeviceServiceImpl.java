package com.yuepei.web.hospital.service.impl;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.utils.DateUtils;
import com.yuepei.common.utils.SecurityUtils;
import com.yuepei.common.utils.ServletUtils;
import com.yuepei.common.utils.ip.IpUtils;
import com.yuepei.system.domain.*;
import com.yuepei.system.domain.vo.*;
import com.yuepei.system.mapper.HospitalDeviceMapper;
import com.yuepei.system.mapper.SysUserMapper;
import com.yuepei.system.mapper.UserLeaseOrderMapper;
import com.yuepei.system.service.ISysUserService;
import com.yuepei.utils.TokenUtils;
import com.yuepei.web.hospital.service.HospitalDeviceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    private TokenUtils tokenUtils;

    @Override
    public List<DeviceType> selectDeviceType(Long userId) {
        return hospitalDeviceMapper.selectDeviceType(userId);
    }

    @Override
    public List<DeviceDetailsVo> selectDeviceTypeDetails(Long deviceTypeId, Long userId) {
        //根据当前账号搜索代理医院id
        SysUser user = sysUserMapper.selectUserById(userId);
        HospitalUser hospitalUser = hospitalDeviceMapper.selectHospitalbyUserName(user.getUserName());
        //搜索该设备数量及对应详细地址
        List<Device> deviceList = hospitalDeviceMapper.selectDeviceTypeDetails(deviceTypeId, hospitalUser.getHospitalId());
        List<DeviceDetailsVo> deviceDetailsVos = new ArrayList<>();
        //遍历分割详细地址，赋值后返回数据
        deviceList.stream().forEach(map -> {
            Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(hospitalUser.getHospitalId());
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
                deviceDetailsVo.setDeviceFullAddress(device_full_address);
                deviceDetailsVo.setDeviceNumber(map.getDeviceNumber());
                deviceDetailsVo.setStatus(map.getStatus());
                deviceDetailsVo.setHospitalId(hospital.getHospitalId());
                deviceDetailsVo.setHospitalName(hospital.getHospitalName());
                deviceDetailsVos.add(deviceDetailsVo);
            }
        });
        return deviceDetailsVos;
    }

    @Override
    public void updateDeviceDetails(DeviceDetailsVo deviceDetailsVo) {
        deviceDetailsVo.setDeviceFullAddress(deviceDetailsVo.getDeviceFllor() + "," +
                deviceDetailsVo.getDeviceDepartment() + "," +
                deviceDetailsVo.getDeviceRoom() + "," +
                deviceDetailsVo.getDeviceBed());
        hospitalDeviceMapper.updateDeviceDetails(deviceDetailsVo.getDeviceNumber(), deviceDetailsVo.getDeviceFullAddress());
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
    public List<UserLeaseOrderVo> selectLeaseOrder(String userName) {
        HospitalUser hospitalUser = hospitalDeviceMapper.selectHospitalbyUserName(userName);
        List<String> numberList = hospitalDeviceMapper.selectLeaseOrder(hospitalUser.getHospitalId());
        List<UserLeaseOrderVo> userLeaseOrderList = userLeaseOrderMapper.selectUserLeaseOrder(numberList);
        return userLeaseOrderList;
    }

    @Override
    public UserLeaseOrderVo selectLeaseOrderDetails(String orderNumber) {
        UserLeaseOrder userLeaseOrder = userLeaseOrderMapper.selectLeaseOrderDetails(orderNumber);
        UserLeaseOrderVo userLeaseOrderVo = new UserLeaseOrderVo();
        BeanUtils.copyProperties(userLeaseOrder, userLeaseOrderVo);
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
                orderVo.setNetAmount(map.getNetAmount());
                orderVo.setDividendRatio(sysUser.getProportion());
                orderVo.setIncomeAmount(sysUser.getProportion()*map.getNetAmount()/100);
                orderVos.add(orderVo);
            });
            totalVo.setEffectiveOrder(orderVos.size());
            Long orderAmount = orderVos.stream().mapToLong(OrderVo::getNetAmount).sum();
            Long dividendAmount = orderVos.stream().mapToLong(OrderVo::getIncomeAmount).sum();
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
                orderVo.setNetAmount(map.getNetAmount());
                orderVo.setDividendRatio(sysUser.getProportion());
                orderVo.setIncomeAmount(sysUser.getProportion()*map.getNetAmount()/100);
                orderVos.add(orderVo);
            });
            totalVo.setEffectiveOrder(orderVos.size());
            Long orderAmount = orderVos.stream().mapToLong(OrderVo::getNetAmount).sum();
            Long dividendAmount = orderVos.stream().mapToLong(OrderVo::getIncomeAmount).sum();
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
                orderVo.setNetAmount(map.getNetAmount());
                orderVo.setDividendRatio(sysUser.getProportion());
                orderVo.setIncomeAmount(sysUser.getProportion()*map.getNetAmount()/100);
                orderVos.add(orderVo);
            });
            totalVo.setEffectiveOrder(orderVos.size());
            Long orderAmount = orderVos.stream().mapToLong(OrderVo::getNetAmount).sum();
            Long dividendAmount = orderVos.stream().mapToLong(OrderVo::getIncomeAmount).sum();
            totalVo.setOrderAmount(orderAmount);
            totalVo.setDividendAmount(dividendAmount);
            totalVo.setOrderVos(orderVos);
        } else {
            List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectRevenueStatistics(deviceNumberList);
            userLeaseOrderList.stream().forEach(map->{
                OrderVo orderVo = new OrderVo();
                orderVo.setOrderNumber(map.getOrderNumber());
                orderVo.setNetAmount(map.getNetAmount());
                orderVo.setDividendRatio(sysUser.getProportion());
                orderVo.setIncomeAmount(sysUser.getProportion()*map.getNetAmount()/100);
                orderVos.add(orderVo);
            });
            totalVo.setEffectiveOrder(orderVos.size());
            Long orderAmount = orderVos.stream().mapToLong(OrderVo::getNetAmount).sum();
            Long dividendAmount = orderVos.stream().mapToLong(OrderVo::getIncomeAmount).sum();
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
            /*AjaxResult ajax = AjaxResult.success();
            return ajax.put(Constants.TOKEN, tokenUtils.createToken(user));*/
            return AjaxResult.success(user);
        }
        return AjaxResult.error("密码不正确，请重新登录");
    }
}
