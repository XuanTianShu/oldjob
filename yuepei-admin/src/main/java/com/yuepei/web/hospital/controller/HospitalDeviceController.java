package com.yuepei.web.hospital.controller;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.system.domain.vo.DeviceDetailsVo;
import com.yuepei.system.service.OrderService;
import com.yuepei.utils.TokenUtils;
import com.yuepei.web.agent.service.AgentService;
import com.yuepei.web.hospital.service.HospitalDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zzy
 * @date 2023/2/9 16:27
 */
@RestController
@RequestMapping("/hospital/user/info")
public class HospitalDeviceController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private HospitalDeviceService hospitalDeviceService;

    /**根据账号密码登录*/
    @GetMapping("/loginHospitalPort")
    private AjaxResult loginHospitalPort(@RequestParam("userName")String userName,
                                         @RequestParam("password")String password){
        return hospitalDeviceService.loginHospitalPort(userName,password);
    }

    /**
    * 查询医院设备类型
    * */
    @GetMapping("/selectDeviceType")
    public AjaxResult selectHospital(HttpServletRequest request){
        SysUser analysis = tokenUtils.analysis(request);
        return AjaxResult.success(hospitalDeviceService.selectDeviceType(analysis.getUserId()));
    }

    /**
     * 查询该医院设备详情
     * */
    @GetMapping("/selectDeviceTypeDetails")
    public AjaxResult selectDeviceTypeDetails(@RequestParam("deviceTypeId") Long deviceTypeId,
                                              HttpServletRequest request){
        SysUser analysis = tokenUtils.analysis(request);
        return AjaxResult.success(hospitalDeviceService.selectDeviceTypeDetails(deviceTypeId, analysis.getUserId()));
    }

    /**
     * 医院端设备详情编辑信息
     * */
    @PostMapping("/updateDeviceDetails")
    public AjaxResult updateDeviceDetails(@RequestBody DeviceDetailsVo deviceDetailsVo){
        hospitalDeviceService.updateDeviceDetails(deviceDetailsVo);
        return AjaxResult.success();
    }

    /**
     * 查询商品订单
     * */
    @GetMapping("/selectGoodsOrder")
    public AjaxResult selectGoodsOrder(HttpServletRequest request){
        SysUser analysis = tokenUtils.analysis(request);
        return AjaxResult.success(hospitalDeviceService.selectGoodsOrder(analysis.getUserId()));
    }

    /**查询商品详细信息*/
    @GetMapping("/selectGoodsOrderDetails")
    private AjaxResult selectGoodsOrderDetails(@RequestParam("orderId")Long orderId){
        return AjaxResult.success(hospitalDeviceService.selectOrderByOrderId(orderId));
    }

    /**
     * 陪护床租借订单
     * */
    @GetMapping("/selectLeaseOrder")
    public AjaxResult selectLeaseOrder(HttpServletRequest request){
        SysUser analysis = tokenUtils.analysis(request);
        return AjaxResult.success(hospitalDeviceService.selectLeaseOrder(analysis.getUserName()));
    }

    /**
     * 陪护床租借详情
     * */
    @GetMapping("/selectLeaseOrderDetails")
    public AjaxResult selectLeaseOrderDetails(@RequestParam("orderNumber")String orderNumber){
        return AjaxResult.success(hospitalDeviceService.selectLeaseOrderDetails(orderNumber));
    }

    /**医院营收统计*/
    @GetMapping("/selectRevenueStatistics")
    public AjaxResult selectRevenueStatistics(@RequestParam("statistics")int statistics,
                                              HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(hospitalDeviceService.selectRevenueStatistics("ohy", statistics));
    }

}
