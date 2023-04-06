package com.yuepei.investor.controller;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.investor.service.AppletInvestorService;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 设备信息
 */
@RestController
@RequestMapping("/wechat/user/investor/device")
public class InvestorDeviceController {

    @Autowired
    private AppletInvestorService appletInvestorService;

    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping("/list")
    public AjaxResult list(HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success();
    }

    /**首页*/
    @GetMapping("/indexPage")
    public AjaxResult indexPage(HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletInvestorService.indexPage(user.getUserId()));
    }

    /**医院营收统计*/
    @GetMapping("/selectRevenueStatistics")
    public AjaxResult selectRevenueStatistics(HttpServletRequest request,
                                              @RequestParam("statistics")int statistics){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletInvestorService.selectRevenueStatistics(user.getUserId(), statistics));
    }

    /**投资人-设备信息*/
    @GetMapping("/selectDeviceType")
    public AjaxResult selectHospital(HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletInvestorService.selectDeviceType(user.getUserId()));
    }
}
