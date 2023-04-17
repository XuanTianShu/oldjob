package com.yuepei.investor.controller;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.investor.service.AppletInvestorService;
import com.yuepei.system.domain.vo.FeedbackInfoVo;
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

    /**投资人-首页-请选择医院*/
    @GetMapping("/selectHospitalName")
    public AjaxResult selectHospitalName(HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletInvestorService.selectHospitalName(user.getUserId()));
    }

    /**首页*/
    @GetMapping("/indexPage")
    public AjaxResult indexPage(HttpServletRequest request,
                                @RequestParam(value = "hospitalId",required = false)Long hospitalId){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletInvestorService.indexPage(user.getUserId(),hospitalId));
    }

    /**医院营收统计*/
    @GetMapping("/selectRevenueStatistics")
    public AjaxResult selectRevenueStatistics(HttpServletRequest request,
                                              @RequestParam("statistics")int statistics){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletInvestorService.selectRevenueStatistics(user.getUserId(), statistics));
    }

    /**投资人-科室下拉框*/
    @GetMapping("/selectDepartment")
    public AjaxResult selectDepartment(HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletInvestorService.selectDepartment(user.getUserId()));
    }

    /**投资人-设备信息*/
    @GetMapping("/selectDeviceType")
    public AjaxResult selectHospital(HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletInvestorService.selectDeviceType(user.getUserId()));
    }

    /**投资人-根据设备类型选择医院*/
    @GetMapping("/selectDeviceTypeByHospital")
    public AjaxResult selectDeviceTypeByHospital(HttpServletRequest request,
                                                 @RequestParam(value = "deviceTypeId",required = false)Long deviceTypeId){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletInvestorService.selectDeviceTypeByHospital(user.getUserId(),deviceTypeId));
    }

    /**投资人-设备管理*/
    @GetMapping("/investorDeviceManage")
    public AjaxResult investorDeviceManage(HttpServletRequest request,
                                           @RequestParam(value = "hospitalId",required = false)Long hospitalId,
                                           @RequestParam(value = "departmentName",required = false,defaultValue = "")String departmentName,
                                           @RequestParam(value = "deviceTypeId",required = false)Long deviceTypeId,
                                           @RequestParam(value = "utilizationRate",required = false)Long utilizationRate){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletInvestorService.investorDeviceManage(user.getUserId(),hospitalId,departmentName,utilizationRate,deviceTypeId));
    }

    /**投资人-个人中心*/
    @GetMapping("/investorPersonalCenter")
    public AjaxResult investorPersonalCenter(HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletInvestorService.investorPersonalCenter(user.getUserId()));
    }

    /**投资人-个人资料*/
    @GetMapping("/investorPersonalData")
    public AjaxResult investorPersonalData(HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletInvestorService.investorPersonalData(user.getUserId()));
    }

    /**查询可分配分成比例*/
    @GetMapping("/selectProportion")
    private AjaxResult selectProportion(HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletInvestorService.selectProportion(user.getUserId()));
    }

    /**投资人-子账户管理列表*/
    @GetMapping("/investorSubAccount")
    private AjaxResult investorSubAccount(HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletInvestorService.investorSubAccount(user.getUserId()));
    }

    /**投资人-拍张上传2*/
    @PostMapping("/investorUploadsFile")
    private AjaxResult investorUploadsFile(@RequestBody FeedbackInfoVo feedbackInfoVo) {
        return AjaxResult.success(appletInvestorService.investorUploadsFile(feedbackInfoVo));
    }

    /**投资人-上传的文档列表*/
    @GetMapping("/investorUploadsFileList")
    private AjaxResult investorUploadsFileList(HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletInvestorService.investorUploadsFileList(user.getUserId()));
    }

    /**投资人-上传文档列表-未处理*/
    @GetMapping("/investorUploadsFileListDetails")
    private AjaxResult investorUploadsFileListDetails(@RequestParam("feedbackId")Long feedbackId){
        return AjaxResult.success(appletInvestorService.investorUploadsFileListDetails(feedbackId));
    }

    /**投资人-租借订单*/
    @GetMapping("/investorLeaseOrder")
    private AjaxResult investorLeaseOrder(HttpServletRequest request,
                                          @RequestParam(value = "status",required = false,defaultValue = "") String status,
                                          @RequestParam(value = "deviceDepartment",required = false,defaultValue = "") String deviceDepartment,
                                          @RequestParam(value = "deviceTypeName",required = false,defaultValue = "") String deviceTypeName,
                                          @RequestParam(value = "nameOrNumber",required = false,defaultValue = "") String nameOrNumber){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletInvestorService.investorLeaseOrder(user.getUserId(),status,deviceDepartment,deviceTypeName,nameOrNumber));
    }


    /**投资人-陪护床-订单详情*/
    @GetMapping("/investorLeaseOrderDetails")
    public AjaxResult investorLeaseOrderDetails(HttpServletRequest request,
                                                @RequestParam(value = "orderNumber",required = false) String orderNumber){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletInvestorService.investorLeaseOrderDetails(orderNumber,user.getUserId()));
    }
}
