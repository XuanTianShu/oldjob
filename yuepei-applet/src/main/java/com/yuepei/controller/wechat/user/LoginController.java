package com.yuepei.controller.wechat.user;
import com.yuepei.common.constant.Constants;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.utils.SecurityUtils;
import com.yuepei.service.LoginService;
import com.yuepei.system.mapper.SysUserMapper;
import com.yuepei.system.service.HospitalDeviceService;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

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
 * @create ：2022/12/2 15:09
 **/
@RestController
@RequestMapping("/wechat/user")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private HospitalDeviceService hospitalDeviceService;

    @Autowired
    private SysUserMapper sysUserMapper;


    @PostMapping("/login")
    public AjaxResult wechatLogin(@NotNull String code ,@RequestBody SysUser sysUser){
        return loginService.login(code,sysUser);
    }


    /**根据账号密码登录*/
    @GetMapping("/loginHospitalPort")
    private AjaxResult loginHospitalPort(@RequestParam("userName")String userName,
                                         @RequestParam("password")String password){
        AjaxResult ajax = new AjaxResult();
        //得到用户密码
        SysUser users = sysUserMapper.getPassword(userName);
        if(users==null||"".equals(users)){
            return AjaxResult.error();
        }
        //解析判断
        boolean isTrue = SecurityUtils.matchesPassword(password, users.getPassword());
        //成功
        if(isTrue){
            //HashMap<String, Object> userIdMap = new HashMap<>();
            //userIdMap.put("userId",users.getUserId());
            //生成Token令牌,存入ajax
            String token = tokenUtils.createToken(users);
            SysUser sysUser = sysUserMapper.selectUserByUserName(userName);
            ajax.put(Constants.TOKEN,token);
            ajax.put("data",sysUser);
            return ajax;
        }
        //失败
        return AjaxResult.error();
        //return hospitalDeviceService.loginHospitalPort(userName,password);
    }

    /**
     * 查询医院设备类型
     * */
    @GetMapping("/selectDeviceType/{userId}")
    public AjaxResult selectHospital(@PathVariable("userId") Long userId){
        System.out.println(hospitalDeviceService.selectDeviceType(userId)+"--------------------------------------");
        return AjaxResult.success(hospitalDeviceService.selectDeviceType(userId));
    }


    /**
     * 查询该医院设备详情
     * */
    @GetMapping("/selectDeviceTypeDetails")
    public AjaxResult selectDeviceTypeDetails(@RequestParam("deviceTypeId") Long deviceTypeId,
                                              @RequestParam(value = "deviceDepartment",required = false,defaultValue = "") String deviceDepartment,
                                              @PathVariable("userId") Long userId){
        return AjaxResult.success(hospitalDeviceService.selectDeviceTypeDetails(deviceTypeId, userId, deviceDepartment));
    }

    /**根据医院查询详细地址*/
    @GetMapping("/selectDeviceAddress")
    private AjaxResult selectDeviceAddress(@RequestParam("hospitalId")Long hospitalId){
        return AjaxResult.success(hospitalDeviceService.selectDeviceAddress(hospitalId));
    }

    /**
     * 医院端设备详情编辑信息
     * */
    @GetMapping("/updateDeviceDetails")
    public AjaxResult updateDeviceDetails(@RequestParam(value = "floorId",required = false,defaultValue = "")Long floorId,
                                          @RequestParam(value = "departmentId",required = false,defaultValue = "")Long departmentId,
                                          @RequestParam(value = "roomId",required = false,defaultValue = "")Long roomId,
                                          @RequestParam(value = "bedId",required = false,defaultValue = "")Long bedId,
                                          @RequestParam(value = "deviceNumber",required = false,defaultValue = "")String deviceNumber
    ){
        hospitalDeviceService.updateDeviceDetails(floorId,departmentId,roomId,bedId,deviceNumber);
        return AjaxResult.success();
    }

    /**
     * 查询商品订单
     * */
    @GetMapping("/selectGoodsOrder/{userId}")
    public AjaxResult selectGoodsOrder(@PathVariable("userId") Long userId){
        return AjaxResult.success(hospitalDeviceService.selectGoodsOrder(userId));
    }

    /**查询商品详细信息*/
    @GetMapping("/selectGoodsOrderDetails")
    private AjaxResult selectGoodsOrderDetails(@RequestParam("orderId")Long orderId){
        return AjaxResult.success(hospitalDeviceService.selectOrderByOrderId(orderId));
    }

    /**设备类型下拉框*/
    @GetMapping("/selectDeviceTypeName/{userId}")
    public AjaxResult selectDeviceTypeName(@PathVariable("userId") Long userId){
        return AjaxResult.success(hospitalDeviceService.selectDeviceTypeName(userId));
    }

    /**选择科室下拉框*/
    @GetMapping("/selectDepartment/{userId}")
    public AjaxResult selectDepartment(@PathVariable("userId") Long userId){
        return AjaxResult.success(hospitalDeviceService.selectDepartment(userId));
    }

    /**
     * 陪护床租借订单
     * */
    @GetMapping("/selectLeaseOrder")
    public AjaxResult selectLeaseOrder(@RequestParam(value = "deviceDepartment",required = false,defaultValue = "") String deviceDepartment,
                                       @RequestParam(value = "deviceTypeName",required = false,defaultValue = "") String deviceTypeName,
                                       @RequestParam(value = "orderNumber",required = false,defaultValue = "") String orderNumber,
                                       @RequestParam("userId") Long userId){
        return AjaxResult.success(hospitalDeviceService.selectLeaseOrder(userId,deviceDepartment,deviceTypeName,orderNumber));
    }

    /**
     * 陪护床租借详情
     * */
    @GetMapping("/selectLeaseOrderDetails")
    public AjaxResult selectLeaseOrderDetails(@RequestParam("orderNumber")String orderNumber,
                                              @RequestParam("userName") Long userId){
        return AjaxResult.success(hospitalDeviceService.selectLeaseOrderDetails(orderNumber, userId));
    }

    /**医院营收统计*/
    @GetMapping("/selectRevenueStatistics")
    public AjaxResult selectRevenueStatistics(@RequestParam("statistics")int statistics,
                                              @RequestParam("userName") String userName){
        return AjaxResult.success(hospitalDeviceService.selectRevenueStatistics(userName, statistics));
    }
}
