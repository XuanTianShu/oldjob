package com.yuepei.controller.wechat.HospitalDeviceController;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.service.HospitalDeviceService;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zzy
 * @date 2023/2/9 16:27
 */
@RestController
@RequestMapping("/hospital/user/info")
public class HospitalDeviceController {

    /*@Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private HospitalDeviceService basicInfoService;

    *//**
    * 查询医院设备类型
    * *//*
    @GetMapping("/selectDeviceType")
    public AjaxResult selectHospital(HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(basicInfoService.selectDeviceType(user.getUserId()));
    }

    *//**
     * 查询该医院设备详情
     * *//*
    @GetMapping("/selectDeviceTypeDetails")
    public AjaxResult selectDeviceTypeDetails(Long deviceTypeId){
        return AjaxResult.success(basicInfoService.selectDeviceTypeDetails(deviceTypeId));
    }*/
}
