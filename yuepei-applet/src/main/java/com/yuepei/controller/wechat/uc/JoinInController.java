package com.yuepei.controller.wechat.uc;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.utils.DateUtils;
import com.yuepei.system.domain.SysUserCooperation;
import com.yuepei.system.service.ISysUserCooperationService;
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
 * @create ：2022/12/20 15:17
 **/
@RestController
@RequestMapping("/wechat/user/join")
public class JoinInController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private ISysUserCooperationService cooperationService;

    @PostMapping("/insertJoinIn")
    public AjaxResult insertJoinIn(HttpServletRequest request , @RequestBody SysUserCooperation cooperation){
        SysUser user = tokenUtils.analysis(request);
        cooperation.setUserId(user.getUserId());
        cooperation.setCreateTime(DateUtils.getNowDate());
        return AjaxResult.success(cooperationService.insertCooperation(cooperation));
    }

    @GetMapping("/selectJoinInList")
    public AjaxResult selectJoinInList(HttpServletRequest request,Integer status){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(cooperationService.selectCooperationByCooperationUserId(user.getUserId(),status));
    }
}
