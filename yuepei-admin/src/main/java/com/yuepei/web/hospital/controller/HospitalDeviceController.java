package com.yuepei.web.hospital.controller;

import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.framework.web.service.SysLoginService;
import com.yuepei.system.domain.vo.DeviceDetailsVo;
import com.yuepei.utils.TokenUtils;
import com.yuepei.web.hospital.service.HospitalDeviceService;
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
    * 查询医院设备类型
    * */
    @GetMapping("/selectDeviceType")
    public TableDataInfo selectHospital(HttpServletRequest request){
        startPage();
        SysUser analysis = tokenUtils.analysis(request);
        return getDataTable(hospitalDeviceService.selectDeviceType(analysis.getUserId()));
    }

    /**
     * 查询该医院设备详情
     * */
    @PostMapping("/selectDeviceTypeDetails")
    public AjaxResult selectDeviceTypeDetails(@RequestParam("deviceTypeId") Long deviceTypeId,
                                              @RequestParam("hospitalId")Long hospitalId){
        return AjaxResult.success(hospitalDeviceService.selectDeviceTypeDetails(deviceTypeId,hospitalId));
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
    @PostMapping("/selectGoodsOrderDetails")
    private AjaxResult selectGoodsOrderDetails(@RequestParam("orderId")Long orderId){
        return AjaxResult.success(hospitalDeviceService.selectOrderByOrderId(orderId));
    }

    /**
     * 陪护床租借订单
     * */
    @PostMapping("/selectLeaseOrder")
    public AjaxResult selectLeaseOrder(@RequestParam("hospitalId")Long hospitalId){
        return AjaxResult.success(hospitalDeviceService.selectLeaseOrder(hospitalId));
    }

    /**
     * 陪护床租借详情
     * */
    @PostMapping("/selectLeaseOrderDetails")
    public AjaxResult selectLeaseOrderDetails(@RequestParam("orderNumber")String orderNumber){
        return AjaxResult.success(hospitalDeviceService.selectLeaseOrderDetails(orderNumber));
    }

    /**医院营收统计*/
    @PostMapping("/selectRevenueStatistics")
    public AjaxResult selectRevenueStatistics(@RequestParam("hospitalId")Long hospitalId,
                                              @RequestParam("statistics")int statistics){
        return AjaxResult.success(hospitalDeviceService.selectRevenueStatistics(hospitalId,statistics));
    }

}
