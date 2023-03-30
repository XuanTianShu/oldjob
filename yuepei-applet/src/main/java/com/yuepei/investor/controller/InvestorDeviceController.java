package com.yuepei.investor.controller;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.investor.service.AppletInvestorService;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
