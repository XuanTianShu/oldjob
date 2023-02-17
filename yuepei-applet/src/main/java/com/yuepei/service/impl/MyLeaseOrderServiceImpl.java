package com.yuepei.service.impl;
import com.yuepei.service.MyLeaseOrderService;
import com.yuepei.system.domain.Device;
import com.yuepei.system.domain.DeviceType;
import com.yuepei.system.domain.UserLeaseOrder;
import com.yuepei.system.domain.vo.LeaseOrderVO;
import com.yuepei.system.mapper.DeviceMapper;
import com.yuepei.system.mapper.DeviceTypeMapper;
import com.yuepei.system.mapper.UserLeaseOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 　　　　 ┏┓       ┏┓+ +
 * 　　　　┏┛┻━━━━━━━┛┻┓ + +
 * 　　　　┃　　　　　　 ┃
 * 　　　　┃　　　━　　　┃ ++ + + +
 * 　　　 █████━█████  ┃+
 * 　　　　┃　　　　　　 ┃ +
 * 　　　　┃　　　┻　　　┃
 * 　　　　┃　　　　　　 ┃ + +
 * 　　　　┗━━┓　　　 ┏━┛
 * 　　　　　　┃　　  ┃
 * 　　　　　　┃　　  ┃ + + + +
 * 　　　　　　┃　　　┃　Code is far away from bug with the animal protection
 * 　　　　　　┃　　　┃ + 　　　　         神兽保佑,代码永无bug
 * 　　　　　　┃　　　┃
 * 　　　　　　┃　　　┃　　+
 * 　　　　　　┃　 　 ┗━━━┓ + +
 * 　　　　　　┃ 　　　　　┣-┓
 * 　　　　　　┃ 　　　　　┏-┛
 * 　　　　　　┗┓┓┏━━━┳┓┏┛ + + + +
 * 　　　　　　 ┃┫┫　 ┃┫┫
 * 　　　　　　 ┗┻┛　 ┗┻┛+ + + +
 * *********************************************************
 *
 * @author ：AK
 * @create ：2023/1/9 18:34
 **/
@Service
public class MyLeaseOrderServiceImpl implements MyLeaseOrderService {
    @Autowired
    private UserLeaseOrderMapper userLeaseOrderMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    @Override
    public List<UserLeaseOrder> userLeaseOrder(String openid, Integer status) {
        return userLeaseOrderMapper.userLeaseOrder(openid,status);
    }

    @Override
    public int insertUserLeaseOrder(String openid, String rows, UserLeaseOrder userLeaseOrder) {
        //修改 格子柜状态
        deviceMapper.updateDeviceByDeviceNumber(rows,userLeaseOrder.getDeviceNumber());
        //创建订单号
        if(userLeaseOrder.getOrderNumber() == null ){
            //创建订单
            String orderNumber = UUID.randomUUID().toString().replace("-", "");
            Device device = deviceMapper.selectDeviceByDeviceNumber(userLeaseOrder.getDeviceNumber());
            DeviceType deviceType = deviceTypeMapper.selectDeviceTypeByDeviceTypeId(device.getDeviceTypeId());
            userLeaseOrder.setDeviceType(deviceType.getDeviceTypeName());
            userLeaseOrder.setLeaseTime(new Date());
            userLeaseOrder.setOrderNumber(orderNumber);
            userLeaseOrder.setOpenid(openid);
            userLeaseOrder.setStatus("0");
            return userLeaseOrderMapper.insertUserLeaseOrder(userLeaseOrder);
        }else {
            //修改订单
            userLeaseOrder.setStatus("1");
            return userLeaseOrderMapper.updateUserLeaseOrderByOrderNumber(userLeaseOrder);
        }
    }

    /**
     * 查询租赁订单
     * @param leaseOrderVO
     * @return
     */
    @Override
    public List<UserLeaseOrder> leaseOrderList(LeaseOrderVO leaseOrderVO) {
        return userLeaseOrderMapper.leaseOrderList(leaseOrderVO);
    }

}
