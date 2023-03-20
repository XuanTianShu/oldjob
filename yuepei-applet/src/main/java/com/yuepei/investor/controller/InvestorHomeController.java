package com.yuepei.investor.controller;

import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.investor.domain.vo.InvestorHospitalVO;
import com.yuepei.investor.service.AppletInvestorService;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 首页
 */
@RestController
@RequestMapping("/wechat/user/investor/home")
public class InvestorHomeController extends BaseController {

    @Autowired
    private AppletInvestorService appletInvestorService;

    @Autowired
    private TokenUtils tokenUtils;

    /**
     * 首页
     * @param request
     * @return
     */
//    @PreAuthorize("@ss.hasPermi('system:device:list')")
    @GetMapping("/list")
    public AjaxResult home(HttpServletRequest request, InvestorHospitalVO investorHospitalVO){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletInvestorService.selectHome(user,investorHospitalVO));
    }

    /**
     * 昨日营收
     * @param request
     * @param hospitalId
     * @return
     */
    @GetMapping("/yesterDayRevenue")
    public AjaxResult yesterdayRevenue(HttpServletRequest request,Long hospitalId){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletInvestorService.yesterdayRevenue(user,hospitalId));
    }

    /**
     * 今日营收
     * @param request
     * @param hospitalId
     * @return
     */
    @GetMapping("/todayRevenue")
    public AjaxResult todayRevenue(HttpServletRequest request,Long hospitalId){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletInvestorService.todayRevenue(user,hospitalId));
    }

    /**
     * 本月营收
     * @param request
     * @param hospitalId
     * @return
     */
    @GetMapping("/monthRevenue")
    public AjaxResult monthRevenue(HttpServletRequest request,Long hospitalId){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletInvestorService.monthRevenue(user,hospitalId));
    }

    /**
     * 累计营收
     * @param request
     * @param hospitalId
     * @return
     */
    @GetMapping("/accumulativeRevenue")
    public AjaxResult accumulativeRevenue(HttpServletRequest request,Long hospitalId){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletInvestorService.accumulativeRevenue(user,hospitalId));
    }

}
