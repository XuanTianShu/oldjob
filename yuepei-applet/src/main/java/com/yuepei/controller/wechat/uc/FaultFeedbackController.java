package com.yuepei.controller.wechat.uc;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.system.domain.SysUserFeedback;
import com.yuepei.system.domain.pojo.SysUserFeedbackPo;
import com.yuepei.system.service.ISysUserFeedbackService;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 　　　　 ┏┓       ┏┓+ +
 * 　　　　┏┛┻━━━━━━━┛┻┓ + +
 * 　　　　┃　　　　　　 ┃
 * 　　　　┃　　　━　　　┃ ++ + + +
 * 　　　 █████━█████  ┃+
 * 　　　　┃　　　　　　 ┃ +
 * 　　　　┃　　　┻　　　┃
 * 　　　　┃　　　　　　 ┃ + +
 * 　　　　┗━━┓　　　 ┏━┛
 * 　　　　　　┃　　  ┃
 * 　　　　　　┃　　  ┃ + + + +
 * 　　　　　　┃　　　┃　Code is far away from bug with the animal protection
 * 　　　　　　┃　　　┃ + 　　　　         神兽保佑,代码永无bug
 * 　　　　　　┃　　　┃
 * 　　　　　　┃　　　┃　　+
 * 　　　　　　┃　 　 ┗━━━┓ + +
 * 　　　　　　┃ 　　　　　┣-┓
 * 　　　　　　┃ 　　　　　┏-┛
 * 　　　　　　┗┓┓┏━━━┳┓┏┛ + + + +
 * 　　　　　　 ┃┫┫　 ┃┫┫
 * 　　　　　　 ┗┻┛　 ┗┻┛+ + + +
 * *********************************************************
 *
 * @author ：AK
 * @create ：2022/12/22 14:40
 **/
@RestController
@RequestMapping("/wechat/user/feedback")
public class FaultFeedbackController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private ISysUserFeedbackService userFeedbackService;


    @PostMapping("/insertFaultFeedback")
    public AjaxResult insertFaultFeed(HttpServletRequest request, @RequestBody SysUserFeedback feedback){
        SysUser user = tokenUtils.analysis(request);
        feedback.setUserId(user.getUserId());
        return AjaxResult.success(userFeedbackService.insertSysUserFeedback(feedback));
    }

    @GetMapping("/selectFaultFeedbackList")
    public AjaxResult selectFaultFeedbackList(HttpServletRequest request,Long status){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(userFeedbackService.selectFaultFeedbackList(user.getUserId(),status));
    }
}
