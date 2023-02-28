package com.yuepei.web.hospital.service.impl;

import com.yuepei.common.constant.Constants;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.utils.DateUtils;
import com.yuepei.common.utils.SecurityUtils;
import com.yuepei.common.utils.ServletUtils;
import com.yuepei.common.utils.StringUtils;
import com.yuepei.common.utils.ip.IpUtils;
import com.yuepei.service.LoginService;
import com.yuepei.system.domain.*;
import com.yuepei.system.domain.vo.DeviceDetailsVo;
import com.yuepei.system.domain.vo.GoodsOrderVo;
import com.yuepei.system.domain.vo.RevenueStatisticsVo;
import com.yuepei.system.domain.vo.UserLeaseOrderVo;
import com.yuepei.system.mapper.HospitalDeviceMapper;
import com.yuepei.system.mapper.OrderMapper;
import com.yuepei.system.mapper.SysUserMapper;
import com.yuepei.system.mapper.UserLeaseOrderMapper;
import com.yuepei.system.service.ISysUserService;
import com.yuepei.utils.TokenUtils;
import com.yuepei.web.hospital.service.HospitalDeviceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    public List<DeviceDetailsVo> selectDeviceTypeDetails(Long deviceTypeId, Long hospitalId) {
        //搜索该设备数量及对应详细地址
        List<Device> deviceList = hospitalDeviceMapper.selectDeviceTypeDetails(deviceTypeId, hospitalId);
        List<DeviceDetailsVo> deviceDetailsVos = new ArrayList<>();
        //遍历分割详细地址，赋值后返回数据
        deviceList.stream().forEach(map -> {
            Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(hospitalId);
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
    public List<UserLeaseOrderVo> selectLeaseOrder(Long hospitalId) {
        List<String> numberList = hospitalDeviceMapper.selectLeaseOrder(hospitalId);
        List<UserLeaseOrderVo> userLeaseOrders = new ArrayList<>();
        for (String deviceNumber : numberList) {
            List<UserLeaseOrderVo> userLeaseOrderList = userLeaseOrderMapper.selectUserLeaseOrder(deviceNumber);
            userLeaseOrders.addAll(userLeaseOrderList);
        }
        return userLeaseOrders;
    }

    @Override
    public UserLeaseOrderVo selectLeaseOrderDetails(String orderNumber) {
        UserLeaseOrder userLeaseOrder = userLeaseOrderMapper.selectLeaseOrderDetails(orderNumber);
        UserLeaseOrderVo userLeaseOrderVo = new UserLeaseOrderVo();
        BeanUtils.copyProperties(userLeaseOrder, userLeaseOrderVo);
        return userLeaseOrderVo;
    }

    @Override
    public List<UserLeaseOrderVo> selectRevenueStatistics(Long hospitalId, int statistics) {
        List<String> numberList = hospitalDeviceMapper.selectLeaseOrder(hospitalId);
        List<UserLeaseOrderVo> userLeaseOrders = new ArrayList<>();
        if (statistics == 1) {
            for (String deviceNumber : numberList) {
                List<UserLeaseOrderVo> userLeaseOrderList = userLeaseOrderMapper.selectRevenueStatistics(deviceNumber);
                userLeaseOrders.addAll(userLeaseOrderList);
            }
        } else if (statistics == 2) {
            for (String deviceNumber : numberList) {
                List<UserLeaseOrderVo> userLeaseOrderList = userLeaseOrderMapper.selectRevenueStatistics2(deviceNumber);
                userLeaseOrders.addAll(userLeaseOrderList);
            }
        } else if (statistics == 3) {
            for (String deviceNumber : numberList) {
                List<UserLeaseOrderVo> userLeaseOrderList = userLeaseOrderMapper.selectRevenueStatistics3(deviceNumber);
                userLeaseOrders.addAll(userLeaseOrderList);
            }
        } else {
            for (String deviceNumber : numberList) {
                List<UserLeaseOrderVo> userLeaseOrderList = userLeaseOrderMapper.selectUserLeaseOrder(deviceNumber);
                userLeaseOrders.addAll(userLeaseOrderList);
            }
        }
        return userLeaseOrders;
    }

    @Override
    public AjaxResult loginHospitalPort(String userName, String password) {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = sysUserMapper.selectUserByUserName(userName);
        if (user == null) {
            return AjaxResult.error("该账号不存在");
        }
        boolean matchesPassword = SecurityUtils.matchesPassword(password, user.getPassword());
        if (matchesPassword) {
            // 用户验证
            SysUser sysUserList = sysUserMapper.selectUserByOpenid(user.getOpenid());
            if (!StringUtils.isNull(sysUserList)) {
                if ("1".equals(sysUserList.getStatus())) {
                    return AjaxResult.error("账号已被封禁");
                }
                SysUser sysUser = new SysUser();
                sysUser.setUserId(sysUserList.getUserId());
                sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
                sysUser.setLoginDate(DateUtils.getNowDate());
                sysUserService.updateUserProfile(sysUser);
                return ajax.put(Constants.TOKEN, tokenUtils.createToken(sysUserList));
            }
            return AjaxResult.error("该账号openid为空");
        }
        return AjaxResult.error("密码不正确，请重新登录");
    }
}
