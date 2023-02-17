package com.yuepei.controller.wechat.iTunes;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.service.MyLeaseOrderService;
import com.yuepei.system.domain.UserLeaseOrder;
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

    @GetMapping("/userLeaseOrder")
    public AjaxResult userLeaseOrder(HttpServletRequest request,Integer status){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(myLeaseOrderService.userLeaseOrder(user.getOpenid(),status));
//        UserLeaseOrder userLeaseOrder = new UserLeaseOrder();
//        userLeaseOrder.setOpenid(user.getOpenid());
//        userLeaseOrder.setStatus(String.valueOf(status));
//        return AjaxResult.success(myLeaseOrderService.leaseOrderList(userLeaseOrder));
    }

    @PostMapping("/insertUserLeaseOrder")
    public AjaxResult insertUserLeaseOrder(HttpServletRequest request, String rows, @RequestBody UserLeaseOrder userLeaseOrder){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(myLeaseOrderService.insertUserLeaseOrder(user.getOpenid(),rows,userLeaseOrder));
    }

    @GetMapping("/checkLeaseOrder")
    public AjaxResult checkLeaseOrder(String deviceNumber) {
        return AjaxResult.success(userDepositOrderMapper.checkLeaseOrderByOpenId(deviceNumber));
    }

    @PostMapping("/updateLeaseOrderStatus")
    public AjaxResult updateLeaseOrderStatus(HttpServletRequest request,UserLeaseOrder userLeaseOrder){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(userLeaseOrderMapper.updateUserLeaseOrderByOrderNumber(userLeaseOrder));
    }

}
