package com.yuepei.controller.wechat.iTunes;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.service.MyDepositService;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 我的押金
 */
@RestController
@RequestMapping("/wechat/user/deposit")
public class MyDepositController {

    @Autowired
    private MyDepositService depositService;

    @Autowired
    private TokenUtils tokenUtils;

    /**
     *
     * 得到用户押金详细
     * @return
     */
    @GetMapping("/selectUserDepositInfo")
    public AjaxResult selectUserDepositInfo(HttpServletRequest request,String status,String depositStatus){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(depositService.selectUserDepositInfo(user.getOpenid(),status,depositStatus));
    }


    /**
     *
     * 得到用户押金详细
     * @return
     */
    @GetMapping("/selectUserDepositDetailInfo")
    public AjaxResult selectUserDepositDetailInfo(HttpServletRequest request,Integer status){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(depositService.selectUserDepositDetailInfo(user.getOpenid(),status));
    }
}
