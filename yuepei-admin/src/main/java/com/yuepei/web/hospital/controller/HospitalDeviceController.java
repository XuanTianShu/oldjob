package com.yuepei.web.hospital.controller;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.system.domain.vo.DeviceDetailsVo;
import com.yuepei.utils.TokenUtils;
import com.yuepei.web.agent.service.AgentService;
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
public class HospitalDeviceController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private HospitalDeviceService HospitalInfoService;

    /**
    * 查询医院设备类型
    * */
    @GetMapping("/selectDeviceType")
    public AjaxResult selectHospital(HttpServletRequest request){
        SysUser analysis = tokenUtils.analysis(request);
        return AjaxResult.success(HospitalInfoService.selectDeviceType(analysis.getUserId()));
    }

    /**
     * 查询该医院设备详情
     * */
    @PostMapping("/selectDeviceTypeDetails")
    public AjaxResult selectDeviceTypeDetails(@RequestParam("deviceTypeId") Long deviceTypeId,
                                              @RequestParam("hospitalId")Long hospitalId){
        return AjaxResult.success(HospitalInfoService.selectDeviceTypeDetails(deviceTypeId,hospitalId));
    }

    /**
     * 医院端设备详情编辑信息
     * */
    @PostMapping("/updateDeviceDetails")
    public AjaxResult updateDeviceDetails(@RequestBody DeviceDetailsVo deviceDetailsVo){
        HospitalInfoService.updateDeviceDetails(deviceDetailsVo);
        return AjaxResult.success();
    }

    /**
     * 查询商品订单
     * */
    @GetMapping("/selectGoodsOrder")
    public AjaxResult selectGoodsOrder(HttpServletRequest request){
        SysUser analysis = tokenUtils.analysis(request);
        return AjaxResult.success(HospitalInfoService.selectGoodsOrder(analysis.getUserId()));
    }

    /**
     * 陪护床租借订单
     * */
    @PostMapping("/selectLeaseOrder")
    public AjaxResult selectLeaseOrder(@RequestParam("hospitalId")Long hospitalId){
        return AjaxResult.success(HospitalInfoService.selectLeaseOrder(hospitalId));
    }

    /**
     * 陪护床租借详情
     * */
    @PostMapping("/selectLeaseOrderDetails")
    public AjaxResult selectLeaseOrderDetails(@RequestParam("orderNumber")String orderNumber){
        return AjaxResult.success(HospitalInfoService.selectLeaseOrderDetails(orderNumber));
    }

    /**医院营收统计*/
    @PostMapping("/selectRevenueStatistics")
    public AjaxResult selectRevenueStatistics(@RequestParam("hospitalId")Long hospitalId,
                                              @RequestParam("statistics")int statistics){
        return AjaxResult.success(HospitalInfoService.selectRevenueStatistics(hospitalId,statistics));
    }

}
