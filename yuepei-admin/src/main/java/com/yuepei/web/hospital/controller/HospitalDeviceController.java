package com.yuepei.web.hospital.controller;

import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.framework.web.service.SysLoginService;
import com.yuepei.system.domain.vo.DeviceDetailsVo;
import com.yuepei.system.service.HospitalDeviceService;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zzy
 * @date 2023/2/9 16:27
 */
@RestController
@RequestMapping("/hospital/user/info")
public class HospitalDeviceController extends BaseController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private HospitalDeviceService hospitalDeviceService;

    @Autowired
    private SysLoginService loginService;

    /**
    */
/**根据账号密码登录*//*

    @GetMapping("/loginHospitalPort")
    private AjaxResult loginHospitalPort(@RequestParam("userName")String userName,
                                         @RequestParam("password")String password){
        return hospitalDeviceService.loginHospitalPort(userName,password);
    }

    */
/**
    * 查询医院设备类型
    * *//*

    @GetMapping("/selectDeviceType")
    public TableDataInfo selectHospital(HttpServletRequest request){
        startPage();
        SysUser analysis = tokenUtils.analysis(request);
        return getDataTable(hospitalDeviceService.selectDeviceType(analysis.getUserId()));
    }

    */
/**
     * 查询该医院设备详情
     * *//*

    @GetMapping("/selectDeviceTypeDetails")
    public AjaxResult selectDeviceTypeDetails(@RequestParam("deviceTypeId") Long deviceTypeId,
                                              @RequestParam(value = "deviceDepartment",required = false,defaultValue = "") String deviceDepartment,
                                              HttpServletRequest request){
        SysUser analysis = tokenUtils.analysis(request);
        return AjaxResult.success(hospitalDeviceService.selectDeviceTypeDetails(deviceTypeId, analysis.getUserId(),deviceDepartment));
    }

    */
/**根据医院查询详细地址*//*

    @GetMapping("/selectDeviceAddress")
    private AjaxResult selectDeviceAddress(@RequestParam("hospitalId")Long hospitalId){
        return AjaxResult.success(hospitalDeviceService.selectDeviceAddress(hospitalId));
    }

    */
/**
     * 医院端设备详情编辑信息
     * *//*

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

    */
/**
     * 查询商品订单
     * *//*

    @GetMapping("/selectGoodsOrder")
    public AjaxResult selectGoodsOrder(HttpServletRequest request){
        SysUser analysis = tokenUtils.analysis(request);
        return AjaxResult.success(hospitalDeviceService.selectGoodsOrder(analysis.getUserId()));
    }

    */
/**查询商品详细信息*//*

    @GetMapping("/selectGoodsOrderDetails")
    private AjaxResult selectGoodsOrderDetails(@RequestParam("orderId")Long orderId){
        return AjaxResult.success(hospitalDeviceService.selectOrderByOrderId(orderId));
    }

    */
/**设备类型下拉框*//*

    @GetMapping("/selectDeviceTypeName")
    public AjaxResult selectDeviceTypeName(){
        return AjaxResult.success(hospitalDeviceService.selectDeviceTypeName());
    }

    */
/**选择科室下拉框*//*

    @GetMapping("/selectDepartment")
    public AjaxResult selectDepartment(HttpServletRequest request){
        SysUser analysis = tokenUtils.analysis(request);
        return AjaxResult.success(hospitalDeviceService.selectDepartment(analysis.getUserName()));
    }

    */
/**
     * 陪护床租借订单
     * *//*

    @GetMapping("/selectLeaseOrder")
    public AjaxResult selectLeaseOrder(@RequestParam(value = "deviceDepartment",required = false,defaultValue = "") String deviceDepartment,
                                       @RequestParam(value = "deviceTypeName",required = false,defaultValue = "") String deviceTypeName,
                                       @RequestParam(value = "orderNumber",required = false,defaultValue = "") String orderNumber,
                                       HttpServletRequest request){
        SysUser analysis = tokenUtils.analysis(request);
        return AjaxResult.success(hospitalDeviceService.selectLeaseOrder(analysis.getUserName(),deviceDepartment,deviceTypeName,orderNumber));
    }

    */
/**
     * 陪护床租借详情
     * *//*

    @GetMapping("/selectLeaseOrderDetails")
    public AjaxResult selectLeaseOrderDetails(@RequestParam("orderNumber")String orderNumber){
        return AjaxResult.success(hospitalDeviceService.selectLeaseOrderDetails(orderNumber));
    }

    */
/**医院营收统计*//*

    @GetMapping("/selectRevenueStatistics")
    public AjaxResult selectRevenueStatistics(@RequestParam("statistics")int statistics,
                                              HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(hospitalDeviceService.selectRevenueStatistics(user.getUserName(), statistics));
    }
*/

}
