package com.yuepei.controller.wechat.iTunes;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.service.MyLeaseOrderService;
import com.yuepei.system.domain.Device;
import com.yuepei.system.domain.UserLeaseOrder;
import com.yuepei.system.mapper.DeviceMapper;
import com.yuepei.system.mapper.UserDepositOrderMapper;
import com.yuepei.system.mapper.UserLeaseOrderMapper;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
 * @create ：2023/1/9 18:30
 **/
@RestController
@RequestMapping("/wechat/user/lease")
public class MyLeaseOrderController {


    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private MyLeaseOrderService myLeaseOrderService;

    @Autowired
    private UserDepositOrderMapper userDepositOrderMapper;

    @Autowired
    private UserLeaseOrderMapper userLeaseOrderMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @GetMapping("/userLeaseOrder")
    public AjaxResult userLeaseOrder(HttpServletRequest request,Integer status){
        SysUser user = tokenUtils.analysis(request);
//        return AjaxResult.success(myLeaseOrderService.userLeaseOrder("oc0od5fazQUUOnkUbxreEkeYopfI",status));
        return AjaxResult.success(myLeaseOrderService.userLeaseOrder(user.getOpenid(),status));
//        UserLeaseOrder userLeaseOrder = new UserLeaseOrder();
//        userLeaseOrder.setOpenid(user.getOpenid());
//        userLeaseOrder.setStatus(String.valueOf(status));
//        return AjaxResult.success(myLeaseOrderService.leaseOrderList(userLeaseOrder));
    }

    /**
     * 新增租赁订单
     * @param request
     * @param rows
     * @param userLeaseOrder
     * @return
     */
    @PostMapping("/insertUserLeaseOrder")
    public AjaxResult insertUserLeaseOrder(HttpServletRequest request, String rows, @RequestBody UserLeaseOrder userLeaseOrder){
        SysUser user = tokenUtils.analysis(request);
        return myLeaseOrderService.insertUserLeaseOrder(user.getOpenid(),rows,userLeaseOrder);
    }

    /**
     * 修改设备电量
     * @param device
     * @return
     */
    @PutMapping
    public AjaxResult updateDeviceElectric(Device device){
        //修改该设备状态
        Device deviceNumber = deviceMapper.selectDeviceByDeviceNumber(device.getDeviceNumber());
        if (deviceNumber.getElectricEarly() > device.getElectric()){
            deviceNumber.setElectric(device.getElectric());
            deviceNumber.setStatus(2L);
            deviceMapper.updateDevice(deviceNumber);
            return AjaxResult.error("出现故障无法借床！");
        }else {
            deviceNumber.setStatus(0L);
            deviceNumber.setElectric(device.getElectric());
            deviceMapper.updateDevice(deviceNumber);
        }
        return AjaxResult.success();
    }


    //FIXME 已废弃
    @GetMapping("/checkLeaseOrder")
    public AjaxResult checkLeaseOrder(String deviceNumber) {
        return AjaxResult.success(userDepositOrderMapper.checkLeaseOrderByOpenId(deviceNumber));
    }

    /**
     * 更新租赁订单
     * @param request
     * @param userLeaseOrder
     * @return
     */
    @PostMapping("/updateLeaseOrderStatus")
    public AjaxResult updateLeaseOrderStatus(HttpServletRequest request,UserLeaseOrder userLeaseOrder){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(userLeaseOrderMapper.updateUserLeaseOrderByOrderNumber(userLeaseOrder));
    }

}
